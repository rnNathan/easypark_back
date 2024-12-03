package com.senac.easypark.controller;

import com.senac.easypark.auth.AuthenticationService;
import com.senac.easypark.auth.JwtService;
import com.senac.easypark.model.dto.LoginDTO;
import com.senac.easypark.model.dto.RegisterDTO;
import com.senac.easypark.model.entities.Acesso;
import com.senac.easypark.model.repository.AcessoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private AcessoRepository repository;


    @Autowired
    private JwtService jwtService;

    /**
     * Método de login padronizado -> Basic Auth
     *
     *  O parâmetro Authentication já encapsula login (username) e senha (password)
     *  Basic <Base64 encoded username and password>
     * @param authentication
     * @return o JWT gerado
     */
    @PostMapping("/authenticate")
    public String authenticate(Authentication authentication) {
        return authenticationService.authenticate(authentication);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO data) {
        try {
            // Cria um objeto de autenticação com o login e a senha
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.getLogin(), data.getSenha());

            // Autentica o usuário usando o AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(usernamePassword);

            // Gera o token JWT
            var token = jwtService.getGenerateToken(authentication);

            // Obtém o usuário autenticado
            Acesso acesso = repository.findByEmail(data.getLogin()).orElseThrow();

            // Cria a resposta com o token e o tipo de acesso
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("accessType", acesso.getTipoAcesso().name());

            // Retorna a resposta
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        }
    }



    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO registerDTO) {
        if (repository.existsByEmail(registerDTO.email())) {
            return ResponseEntity.badRequest().body("E-mail já cadastrado");
        }

        String encryptedPassword = passwordEncoder.encode(registerDTO.senha());
        Acesso novoAcesso = new Acesso(registerDTO.nome(), encryptedPassword, registerDTO.email(), registerDTO.tipoAcesso());

        repository.save(novoAcesso);

        return ResponseEntity.ok().build();
    }

}