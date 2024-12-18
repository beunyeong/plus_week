package com.example.demo.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordEncoder {

    // 1. 사용가 입력한 비밀번호 암호화
    public static String encode(String rawPassword) {
        return BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, rawPassword.toCharArray());
    }

    // 2. 사용자가 입력한 비밀번호와, 암호화된 비밀번호 비교
    public static boolean matches(String rawPassword, String encodedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword);
        return result.verified;
    }
}