package com.soos.nojam.user.controller;

import com.soos.nojam.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.soos.nojam.user.dto.UserDtos.UserLoginRequest;
import static com.soos.nojam.user.dto.UserDtos.UserRequestDto;
import static com.soos.nojam.user.dto.UserDtos.UserResponseDto;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginRequest> login(
            HttpSession session,
            @RequestBody
            UserLoginRequest userLoginRequest) {

        UserResponseDto userResponseDto
                = userService.login(userLoginRequest);

        session.setAttribute("userId", userResponseDto.id());
        session.setMaxInactiveInterval(30000);

        return ResponseEntity.status(HttpStatus.OK).body(userLoginRequest);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody UserRequestDto userRequestDto) {
        userService.createUser(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{userId}/introduction")
    public ResponseEntity<Void> updateUserIntroduction(
            @PathVariable Long userId,
            @RequestParam String introduction,
            HttpSession session
    ) {
        Long loginUserId = (Long) session.getAttribute("userId");
        if (loginUserId == null || !loginUserId.equals(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        userService.updateUserIntroduction(userId, introduction);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserInfo(@PathVariable("userId") Long userId) {
        UserResponseDto userResponseDto = userService.getUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }
}
