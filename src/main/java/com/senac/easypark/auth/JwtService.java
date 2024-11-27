package com.senac.easypark.auth;

import com.senac.easypark.model.entities.Acesso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;

    @Autowired
    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public  String getGenerateToken(Authentication authentication) {
        Instant now = Instant.now();

        // será usado para definir tempo do token
        long dezHorasEmSegundos = 36000L;

        String roles = authentication
                .getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors
                        .joining(" "));

        Acesso acessoAutenticado = (Acesso) authentication.getPrincipal();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("easypark")			                // emissor do token
                .issuedAt(now) 						            // data/hora em que o token foi emitido
                .expiresAt(now.plusSeconds(dezHorasEmSegundos)) // expiração do token, em segundos.
                .subject(authentication.getName())              // nome do usuário
                .claim("roles", roles)                           // perfis ou permissões (roles)
                .claim("idAcesso", acessoAutenticado.getId()) // mais propriedades adicionais no token
                .build();

        return jwtEncoder.encode(
                        JwtEncoderParameters.from(claims))
                .getTokenValue();
    }
}

