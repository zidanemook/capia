package com.game.capia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import com.game.capia.core.advice.MyLogAdvice;
import com.game.capia.core.advice.MyValidAdvice;
import com.game.capia.core.config.MyFilterRegisterConfig;
import com.game.capia.core.config.MySecurityConfig;
import com.game.capia.core.dummy.DummyEntity;
import com.game.capia.dto.user.UserRequest;
import com.game.capia.dto.user.UserResponse;
import com.game.capia.core.MyWithMockUser;
import com.game.capia.model.user.User;
import com.game.capia.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @WebMvcTest는 웹 계층 컴포넌트만 테스트로 가져옴
 */

@ActiveProfiles("test")
@EnableAspectJAutoProxy // AOP 활성화
@Import({
        MyValidAdvice.class,
        MyLogAdvice.class,
        MySecurityConfig.class,
        MyFilterRegisterConfig.class
}) // Advice 와 Security 설정 가져오기
@WebMvcTest(
        // 필요한 Controller 가져오기, 특정 필터를 제외하기
        controllers = {UserController.class}
)
public class UserControllerUnitTest extends DummyEntity {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper om;
    @MockBean
    private UserService userService;

    @Test
    public void join_test() throws Exception {
        // 준비
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("cos");
        userRequest.setPassword("1234");
        String requestBody = om.writeValueAsString(userRequest);

        // 가정해볼께
        User cos = newMockUser(1L,"cos", "코스");
        UserResponse.JoinOutDTO joinOutDTO = new UserResponse.JoinOutDTO(cos);
        Mockito.when(userService.회원가입(any())).thenReturn(joinOutDTO);

        // 테스트진행
        ResultActions resultActions = mvc
                .perform(post("/join").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + responseBody);

        // 검증해볼께
        resultActions.andExpect(jsonPath("$.data.id").value(1L));
        resultActions.andExpect(jsonPath("$.data.username").value("cos"));
        resultActions.andExpect(jsonPath("$.data.fullName").value("코스"));
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void login_test() throws Exception {
        // given
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("cos");
        userRequest.setPassword("1234");
        String requestBody = om.writeValueAsString(userRequest);

        // stub
        Mockito.when(userService.로그인(any())).thenReturn(true);

        // when
        ResultActions resultActions = mvc
                .perform(post("/login").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + responseBody);

        // then

        resultActions.andExpect(status().isOk());
    }

    @MyWithMockUser(id = 1L, username = "cos", role = "USER", fullName = "코스")
    //@WithMockUser(value = "ssar", password = "1234", roles = "USER")
    @Test
    public void detail_test() throws Exception {
        // given
        Long id = 1L;

        // stub
        User cos = newMockUser(1L,"cos", "코스");
        UserResponse.DetailOutDTO detailOutDTO = new UserResponse.DetailOutDTO(cos);
        Mockito.when(userService.회원상세보기(any())).thenReturn(detailOutDTO);

        // when
        ResultActions resultActions = mvc
                .perform(get("/s/user/"+id));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + responseBody);

        // then
        resultActions.andExpect(jsonPath("$.data.id").value(1L));
        resultActions.andExpect(jsonPath("$.data.username").value("cos"));
        resultActions.andExpect(jsonPath("$.data.email").value("cos@nate.com"));
        resultActions.andExpect(jsonPath("$.data.fullName").value("코스"));
        resultActions.andExpect(jsonPath("$.data.role").value("USER"));
        resultActions.andExpect(status().isOk());
    }
}
