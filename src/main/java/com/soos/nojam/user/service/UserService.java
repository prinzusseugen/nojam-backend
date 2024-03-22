package com.soos.nojam.user.service;

import com.soos.nojam.exception.BusinessException;
import com.soos.nojam.user.entity.User;
import com.soos.nojam.user.repository.UserRepository;
import com.soos.nojam.user.security.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.soos.nojam.exception.ExceptionCode.USER_EMAIL_DUPLICATION;
import static com.soos.nojam.exception.ExceptionCode.USER_EMAIL_NOT_MATCHED;
import static com.soos.nojam.exception.ExceptionCode.USER_NICKNAME_DUPLICATION;
import static com.soos.nojam.exception.ExceptionCode.USER_NOT_FOUND;
import static com.soos.nojam.exception.ExceptionCode.USER_PASSWORD_NOT_MATCHED;
import static com.soos.nojam.user.dto.UserDtos.UserLoginRequest;
import static com.soos.nojam.user.dto.UserDtos.UserRequestDto;
import static com.soos.nojam.user.dto.UserDtos.UserResponseDto;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createUser(final UserRequestDto userRequestDto) {
        validateEmail(userRequestDto.email());
        validateNickname(userRequestDto.nickname());
        String encodedPassword = passwordEncoder.encodePassword(userRequestDto.password());

        userRepository.save(User.builder()
                .email(userRequestDto.email())
                .nickname(userRequestDto.nickname())
                .password(encodedPassword)
                .build()
        );
    }

    public UserResponseDto getUser(final Long id) {
        User user = existsById(id);

        return UserResponseDto.toResponse(user);
    }

    @Transactional
    public void updateUserIntroduction(final Long id, final String introduction) {
        User user = existsById(id);

        user.updateIntroduction(introduction);
    }

    public UserResponseDto login(final UserLoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException(USER_EMAIL_NOT_MATCHED));
        if (!user.doesMatchPassword(request.password(), passwordEncoder)) {
            throw new BusinessException(USER_PASSWORD_NOT_MATCHED);
        }

        return UserResponseDto.toResponse(user);
    }

    private void validateEmail(final String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(USER_EMAIL_DUPLICATION);
        }
    }

    private void validateNickname(final String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new BusinessException(USER_NICKNAME_DUPLICATION);
        }
    }

    private User existsById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
        return user;
    }
}
