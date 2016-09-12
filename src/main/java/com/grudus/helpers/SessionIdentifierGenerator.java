package com.grudus.helpers;


import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.SecureRandom;

public class SessionIdentifierGenerator {

    private static final char[] POSSIBLE_CHARACTERS = new char[62];

    private SecureRandom random = new SecureRandom();

    static {
        for (int i = 0; i < 26; i++) {
            POSSIBLE_CHARACTERS[i] = (char) ('a' + i);
            POSSIBLE_CHARACTERS[26+i] = (char) ('A' + i);
            if (i < 10)
                POSSIBLE_CHARACTERS[52+i] = (char) ('0' + i);
        }
    }


    public String nextRandomString(final int length) {
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            builder.append(POSSIBLE_CHARACTERS[random.nextInt(POSSIBLE_CHARACTERS.length)]);
        return builder.toString();
    }

    public String nextSessionId() {
        return nextRandomString(32);
    }
}
