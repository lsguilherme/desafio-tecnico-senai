package com.example.desafiosenai.utils;

import org.springframework.stereotype.Component;

@Component
public class CodeNormalizer {

    public String normalizedCode(String code) {
        if (code == null || code.trim().isEmpty()){
            throw new IllegalArgumentException("O código não pode estar em branco.");
        }
        if (!code.matches("^[a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException("Código inválido! Deve conter apenas letras e números.");
        }
        return code.toLowerCase();
    }
}
