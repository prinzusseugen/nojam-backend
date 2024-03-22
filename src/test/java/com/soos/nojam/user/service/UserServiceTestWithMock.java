package com.soos.nojam.user.service;

import com.soos.nojam.exception.BusinessException;
import com.soos.nojam.user.entity.User;
import com.soos.nojam.user.repository.UserRepository;
import com.soos.nojam.user.security.PasswordEncoder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.soos.nojam.user.dto.UserDtos.UserLoginRequest;
import static com.soos.nojam.user.dto.UserDtos.UserRequestDto;
import static com.soos.nojam.user.dto.UserDtos.UserResponseDto;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTestWithMock {
    @InjectMocks
    UserService userService;
    @Mock
    UserRepository userRepository;
    @Spy
    PasswordEncoder passwordEncoder;

    final String email = "asdf@gmail.com";
    final String nickname = "qwer";
    final String password = "1q2w3e4r";

    @Test
    void 이메일_닉네임_비밀번호를_입력한_유저는_회원가입_할_수_있다() {
        assertDoesNotThrow(() -> {
            userService.createUser(new UserRequestDto(email, nickname, password));
        });

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void 이메일은_중복일_수_없다() {
        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThrowsExactly(BusinessException.class, () -> {
            userService.createUser(new UserRequestDto(email, nickname, password));
        });
    }

    @Test
    void 닉네임은_중복일_수_없다() {
        when(userRepository.existsByNickname(nickname)).thenReturn(true);

        assertThrowsExactly(BusinessException.class, () -> {
            userService.createUser(new UserRequestDto(email, nickname, password));
        });
    }

    @Test
    void 유저의_소개글을_수정한다() {
        // given
        User user = User.builder()
                .nickname(nickname)
                .email(email)
                .password(password)
                .build();
        String introduce = "소개글";

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        // when
        userService.updateUserIntroduction(1L, introduce);

        // then
        assertEquals(user.getIntroduction(), introduce);
    }

    @Test
    void 존재하지_않는_유저는_소개글을_수정할_수_없다() {
        String introduce = "소개글";

        assertThrowsExactly(BusinessException.class, () -> {
            userService.updateUserIntroduction(1L, introduce);
        });
    }

    @Test
    void 존재하는_유저를_조회한다() {
        User user = User.builder()
                .nickname(nickname)
                .email(email)
                .password(password)
                .build();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        UserResponseDto responseDto = userService.getUser(1L);

        assertSoftly((softly) -> {
            softly.assertThat(responseDto.email()).isEqualTo(email);
            softly.assertThat(responseDto.nickname()).isEqualTo(nickname);
        });
    }

    @Test
    void 존재하지_않는_유저는_조회_할_수_없다() {
        assertThrowsExactly(BusinessException.class, () ->
                userService.getUser(1L));
    }

    @Test
    void 이메일과_비밀번호를_정확하게_입력하면_로그인_할_수_있다() {
        String encodePassword = passwordEncoder.encodePassword(password);
        User user = User.builder()
                .email(email)
                .nickname(nickname)
                .password(encodePassword)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserLoginRequest userLoginRequest = new UserLoginRequest(email, password);
        UserResponseDto userResponseDto = userService.login(userLoginRequest);

        assertSoftly((softly) -> {
            softly.assertThat(userResponseDto.email()).isEqualTo(email);
            softly.assertThat(userResponseDto.nickname()).isEqualTo(nickname);
        });
    }

    @Test
    void 비밀번호가_다르면_로그인_할_수_없다() {
        String encodePassword = passwordEncoder.encodePassword(password);
        User user = User.builder()
                .email(email)
                .nickname(nickname)
                .password(encodePassword)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserLoginRequest userLoginRequest = new UserLoginRequest(email, password + "1");
        assertThrowsExactly(BusinessException.class, () ->
                userService.login(userLoginRequest));
    }
}