package com.grudus.helpers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grudus.entities.User;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class JsonHelper {

    public static final String CHARSET = "utf-8";

    public static String userToJsonString(User user) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(user);
    }

    public static byte[] userToJsonBytes(User user) throws JsonProcessingException, UnsupportedEncodingException {
        return userToJsonString(user).getBytes(CHARSET);
    }

    public static User fromJsonToUser(byte[] userBytes) throws IOException {
        return new ObjectMapper().readValue(new String(userBytes, CHARSET), User.class);
    }
}
