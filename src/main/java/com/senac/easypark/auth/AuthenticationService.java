package com.senac.easypark.auth;

import com.senac.easypark.exception.EstacionamentoException;
import com.senac.easypark.model.entities.Acesso;
import com.senac.easypark.model.repository.AcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService {

    private final JwtService jwtService;

    @Autowired
    private AcessoRepository acessoRepository;

    public AuthenticationService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public String authenticate(Authentication authentication) {
        return jwtService.getGenerateToken(authentication);
    }

    public Acesso getUsuarioAutenticado() throws EstacionamentoException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Acesso acessoAutenticado = null;

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            Jwt jwt = (Jwt) principal;
            String login = jwt.getClaim("sub");

            acessoAutenticado = acessoRepository.findByEmail(login)
                    .orElseThrow(() -> new EstacionamentoException("Usuário não encontrado"));
        }

        if(acessoAutenticado == null) {
            throw new EstacionamentoException("Usuário não encontrado");
        }

        return acessoAutenticado;
    }
}