package com.attendifyserver.attendifyserver.util;

import java.security.SecureRandom;

public final class ClassCodeGenerator {

    private static final String CHAR_POOL = "abcdefghjkmnpqrstuvwxyz23456789";
    private static final int CODE_LENGTH = 7;

    private static final SecureRandom RANDOM = new SecureRandom();

    private ClassCodeGenerator() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static String generate() {
        StringBuilder codeBuilder = new StringBuilder(CODE_LENGTH);
        boolean hasLetter = false;
        boolean hasNumber = false;

        while (!hasLetter || !hasNumber) {
            codeBuilder.setLength(0);
            hasLetter = false;
            hasNumber = false;

            for (int i = 0; i < CODE_LENGTH; i++) {
                char randomChar = CHAR_POOL.charAt(RANDOM.nextInt(CHAR_POOL.length()));
                codeBuilder.append(randomChar);

                if (Character.isLetter(randomChar)) hasLetter = true;
                if (Character.isDigit(randomChar)) hasNumber = true;
            }
        }
        return codeBuilder.toString().toUpperCase();
    }
}