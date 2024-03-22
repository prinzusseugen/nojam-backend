package com.soos.nojam.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soos.nojam.exception.BusinessException;
import com.soos.nojam.user.dto.UserDtos.UserLoginRequest;
import com.soos.nojam.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static com.soos.nojam.exception.ExceptionCode.USER_EMAIL_DUPLICATION;
import static com.soos.nojam.exception.ExceptionCode.USER_LOGIN_FAILED;
import static com.soos.nojam.exception.ExceptionCode.USER_NICKNAME_DUPLICATION;
import static com.soos.nojam.exception.ExceptionCode.USER_NOT_FOUND;
import static com.soos.nojam.user.dto.UserDtos.UserRequestDto;
import static com.soos.nojam.user.dto.UserDtos.UserResponseDto;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    private final String URI_USER = "/api/user";
    private final String email = "asd@gmail.com";
    private final String nickname = "asdf";
    private final String password = "1q2w3e4r";
    private final Long userId = 1L;
    private final String introduction = "안녕하세요.";
    private final Long sessionUserId = 1L;
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private HttpSession session;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 로그인_성공_200() throws Exception {
        // given
        UserLoginRequest userLoginRequest =
                new UserLoginRequest(email, password);

        when(userService.login(userLoginRequest))
                .thenReturn(new UserResponseDto(1L, email, nickname));

        // when & then
        mockMvc.perform(post(URI_USER + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginRequest)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 로그인_실패_401() throws Exception {
        // given
        UserLoginRequest userLoginRequest =
                new UserLoginRequest(email, password);

        when(userService.login(userLoginRequest))
                .thenThrow(new BusinessException(USER_LOGIN_FAILED));

        // when & then
        mockMvc.perform(post(URI_USER + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginRequest)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    void 로그아웃_성공_200() throws Exception {
        // given
        MockHttpSession httpSession = new MockHttpSession();
        httpSession.setAttribute("userId", sessionUserId);

        // when & then
        mockMvc.perform(post(URI_USER + "/logout")
                        .session(httpSession))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 유저_생성_성공_201() throws Exception {
        // given
        UserRequestDto userRequestDto =
                new UserRequestDto(email, nickname, password);

        // when & then
        mockMvc.perform(post(URI_USER + "/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void 유저_생성_실패_중복_이메일_409() throws Exception {
        // given
        UserRequestDto userRequestDto =
                new UserRequestDto(email, nickname, password);

        doThrow(new BusinessException(USER_EMAIL_DUPLICATION))
                .when(userService).createUser(userRequestDto);

        // when & then
        mockMvc.perform(post(URI_USER + "/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    void 유저_생성_실패_중복_닉네임_409() throws Exception {
        // given
        UserRequestDto userRequestDto =
                new UserRequestDto(email, nickname, password);

        doThrow(new BusinessException(USER_NICKNAME_DUPLICATION))
                .when(userService).createUser(userRequestDto);


        // when & then
        mockMvc.perform(post(URI_USER + "/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    void 유저_정보_업데이트_200() throws Exception {
        MockHttpSession httpSession = new MockHttpSession();
        httpSession.setAttribute("userId", sessionUserId);

        // when & then
        mockMvc.perform(patch(URI_USER + "/" + userId + "/introduction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("introduction", introduction)
                        .session(httpSession))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 유저_정보_업데이트_실패_세션_없음_401() throws Exception {
        // when & then
        mockMvc.perform(patch(URI_USER + "/" + userId + "/introduction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("introduction", introduction))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    void 유저_정보_업데이트_실패_유저_없음_404() throws Exception {

        MockHttpSession httpSession = new MockHttpSession();
        httpSession.setAttribute("userId", sessionUserId);

        doThrow(new BusinessException(USER_NOT_FOUND))
                .when(userService).updateUserIntroduction(userId, introduction);

        // when & then
        mockMvc.perform(patch(URI_USER + "/" + userId + "/introduction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("introduction", introduction)
                        .session(httpSession))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void 유저_정보_획득_성공_200() throws Exception {
        // given
        when(userService.getUser(userId))
                .thenReturn(new UserResponseDto(userId, email, nickname));

        // when & then
        mockMvc.perform(get(URI_USER + "/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 유저_정보_획득_실패_404() throws Exception {
        // given
        when(userService.getUser(userId))
                .thenThrow(new BusinessException(USER_NOT_FOUND));

        // when & then
        mockMvc.perform(get(URI_USER + "/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}