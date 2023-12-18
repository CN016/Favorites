package com.favorites.favorites.utils;

public class PasswordUtil {

    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!.])(?=.*[a-zA-Z0-9@#$%^&+=!]).{8,16}$";

    public static boolean validatePassword(String password) {
        return password.matches(PASSWORD_PATTERN);
    }

}
