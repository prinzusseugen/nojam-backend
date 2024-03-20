package com.soos.nojam.user.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    // given
    final String email = "asdf@gmail.com";
    final String nickname = "qwer";
    final String password = "1q2w3e4r";

    @Test
    void 유저_이메일은_없을_수_없다() {
        // when
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> {
                    User.builder()
                            .nickname(nickname)
                            .password(password)
                            .build();
                });

        // then
        assertEquals("이메일은 필수입니다.", exception.getMessage());
    }

    @Test
    void 유저_이메일은_공백일_수_없다() {
        // given
        String invalidEmail = " ";

        // expected
        assertThrowsExactly(IllegalArgumentException.class, () -> {
            User.builder()
                    .email(invalidEmail)
                    .nickname(nickname)
                    .password(password)
                    .build();
        }, "이메일은 필수입니다.");

    }

    @Test
    void 유저_닉네임은_없을_수_없다() {
        // when
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> {
                    User.builder()
                            .email(email)
                            .password(password)
                            .build();
                });

        // then
        assertEquals("닉네임은 필수입니다.", exception.getMessage());
    }

    @Test
    void 유저_닉네임은_공백일_수_없다() {
        // given
        String invalidNickname = "  ";

        // when
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> {
                    User.builder()
                            .email(email)
                            .nickname(invalidNickname)
                            .password(password)
                            .build();
                });

        // then
        assertEquals("닉네임은 필수입니다.", exception.getMessage());
    }

    @Test
    void 유저_비밀번호는_없을_수_없다() {
        // when
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> {
                    User.builder()
                            .email(email)
                            .nickname(nickname)
                            .build();
                });

        // then
        assertEquals("비밀번호는 필수입니다.", exception.getMessage());
    }

    @Test
    void 유저_비밀번호는_공백일_수_없다() {
        // given
        String invalidPassword = " ";

        // when
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> {
                    User.builder()
                            .email(email)
                            .nickname(nickname)
                            .password(invalidPassword)
                            .build();
                });

        // then
        assertEquals("비밀번호는 필수입니다.", exception.getMessage());
    }

    @Test
    void 유저는_이메일_닉네임_패스워드가_필요하다() {
        // when
        User user = User.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .build();

        // then
        assertEquals(email, user.getEmail());
        assertEquals(nickname, user.getNickname());
        assertEquals(password, user.getPassword());
    }
}