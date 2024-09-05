package com.projects.contact_api.helper;

import java.security.SecureRandom;

public class LinkGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int STRING_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();

    public static String generateLink() {
        String randomString;

        StringBuilder sb = new StringBuilder(STRING_LENGTH);
        for (int i = 0; i < STRING_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        randomString = sb.toString();

        return randomString;
    }
}
