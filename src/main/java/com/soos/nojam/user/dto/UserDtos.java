package com.soos.nojam.user.dto;

import com.soos.nojam.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDtos() {
    public record UserLoginRequest(
            @Email
            String email,
            String password
    ) {
    }

    public record UserRequestDto(
            @Email
            String email,
            @NotBlank
            String nickname,
            @NotBlank
            String password
    ) {
    }

    public record UserResponseDto(
            Long id,
            String email,
            String nickname
    ) {

        public static UserResponseDto toResponse(final User user) {
            return new UserResponseDto(user.getId(), user.getEmail(), user.getNickname());
        }
    }


}
