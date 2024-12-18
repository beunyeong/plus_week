package com.example.demo.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderTest {

    // 1. 암호화된 비민번호는 null이 아니여야한다.
    @Test
    void testEncode_NotNull() {
        String rawPassword = "abc123";
        String encodedPassword = PasswordEncoder.encode(rawPassword);

        assertNotNull(encodedPassword, "암호화된 비밀번호는 null이 아니여야 합니다.");

    }


    // 2.사용자가 입력한 비밀번호와, 암호화된 비밀번호는 달라야 한다.
    @Test
    void testEncode_NotSameAsRawPassword() {
        String rawPassword = "abc456";
        String encodedPassword = PasswordEncoder.encode(rawPassword);

        assertNotEquals(encodedPassword, rawPassword, "암호화된 비밀번호는 사용자가 입력한 비밀번호와 달라야 합니다.");
    }

}