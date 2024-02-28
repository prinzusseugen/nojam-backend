package com.soos.nojam.global.auth;

import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


class PasswordEncoderTest {
    PasswordEncoder passwordEncoder = new PasswordEncoder();

    @DisplayName("암호화 전 패스워드와 암호화 후 디코딩 한 패스워드가 같은지 확인")
    @Test
    void 패스워드_암호화_확인(){
        String password = "1q2w3e4r";
        String encodedPassword = passwordEncoder.encodePassword(password);
        System.out.println(encodedPassword);
        assertTrue(passwordEncoder.verifyPassword(password, encodedPassword));

    }

}