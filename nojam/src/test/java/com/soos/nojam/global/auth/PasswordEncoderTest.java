package com.soos.nojam.global.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class PasswordEncoderTest {
    PasswordEncoder passwordEncoder = new PasswordEncoder();

    @DisplayName("암호화 전 패스워드와 암호화 후 디코딩 한 패스워드가 같은지 확인")
    @Test
    void 패스워드_암호화한다() {
        // given
        String password = "1q2w3e4r";
        String encodedPassword = passwordEncoder.encodePassword(password);

        // when & then
        assertNotEquals(password, encodedPassword);
    }

    @Test
    void 패스워드를_복호화하여_검증한다() {
        // given
        String password = "1q2w3e4r";
        String encodedPassword = passwordEncoder.encodePassword(password);

        // when & then
        assertTrue(passwordEncoder.verifyPassword(password, encodedPassword));
    }


    @Test
    void 암호화된_패스워드가_일치하지_않는다() {
        // given
        String password = "1q2w3e4r";
        String encodedPassword = passwordEncoder.encodePassword(password);

        // when & then
        assertFalse(passwordEncoder.verifyPassword(password + "a", encodedPassword));
    }

    @Test
    void 패스워드는_공백일_수_없다() {
        // given
        String password = " ";

        // when & then
        assertThrowsExactly(IllegalArgumentException.class, () -> {
            passwordEncoder.encodePassword(password);
        });
    }

    @Test
    void 패스워드는_없을_수_없다() {
        // given
        String password = null;

        // when & then
        assertThrowsExactly(IllegalArgumentException.class, () -> {
            passwordEncoder.encodePassword(password);
        });
    }
}