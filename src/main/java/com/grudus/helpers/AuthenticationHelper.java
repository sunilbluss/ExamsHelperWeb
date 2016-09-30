package com.grudus.helpers;

import com.grudus.configuration.authentication.UserAuthenticationToken;
import com.grudus.helpers.exceptions.UserAuthenticationException;

public class AuthenticationHelper {

    public static void checkAuthority(UserAuthenticationToken principal, String username) throws UserAuthenticationException {
        if (!(principal != null && (principal.isAdmin() || principal.getName().equals(username))))
            throw new UserAuthenticationException(username);
    }
}
