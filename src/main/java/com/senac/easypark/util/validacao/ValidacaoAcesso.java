package com.senac.easypark.util.validacao;

import com.senac.easypark.model.entities.Acesso;

public class ValidacaoAcesso {

    // Verifica se o email é válido
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email != null && email.matches(emailRegex);
    }

    // Verifica se um campo não é nulo ou vazio
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    // Verifica se o nome tem um tamanho mínimo
    public static boolean isValidLength(String name, int minLength) {
        return name != null && name.length() >= minLength;
    }

    // Valida os campos do Acesso
    public static void validarAcesso(Acesso acesso) {
        if (!isNotEmpty(acesso.getUsername())) {
            throw new IllegalArgumentException("Username não pode ser vazio");
        }
        if (!isNotEmpty(acesso.getSenha()) || !isValidLength(acesso.getSenha(), 6)) {
            throw new IllegalArgumentException("Senha deve ter exatamente 6 caracteres");
        }
        if (!isValidEmail(acesso.getEmail())) {
            throw new IllegalArgumentException("Email inválido");
        }
        if (acesso.getTipoAcesso() == null) {
            throw new IllegalArgumentException("Tipo de acesso não pode ser nulo");
        }
    }
}