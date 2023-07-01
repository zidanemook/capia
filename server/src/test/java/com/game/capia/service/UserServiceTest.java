package com.game.capia.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import com.game.capia.core.auth.session.MyUserDetails;
import com.game.capia.core.dummy.DummyEntity;
import com.game.capia.dto.user.UserRequest;
import com.game.capia.dto.user.UserResponse;
import com.game.capia.model.user.User;
import com.game.capia.model.user.UserRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest extends DummyEntity {

    // 진짜 userService 객체를 만들고 Mock로 Load된 모든 객체를 userService에 주입
    @InjectMocks
    private UserService userService;

    // 진짜 객체를 만들어서 Mockito 환경에 Load
    @Mock
    private UserRepository userRepository;

    // 가짜 객체를 만들어서 Mockito 환경에 Load
    @Mock
    private AuthenticationManager authenticationManager;

    // 진짜 객체를 만들어서 Mockito 환경에 Load
    @Spy
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void hello_test(){
        String pw = "1234";
        String enc = bCryptPasswordEncoder.encode(pw);
        System.out.println(enc);
    }

    @Test
    public void 회원가입_test() throws Exception{

        // given
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("cos");
        userRequest.setPassword("1234");

        // stub 1
        Mockito.when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        // stub 2
        User cos = newMockUser(1L, "cos", "코스");
        Mockito.when(userRepository.save(any())).thenReturn(cos);

        // when
        UserResponse.JoinOutDTO joinOutDTO = userService.회원가입(userRequest);

        // then
        Assertions.assertThat(joinOutDTO.getId()).isEqualTo(1L);
        Assertions.assertThat(joinOutDTO.getUsername()).isEqualTo("cos");
        Assertions.assertThat(joinOutDTO.getFullName()).isEqualTo("코스");
    }

    @Test
    public void 로그인_test() throws Exception{
        // given
        UserRequest loginInDTO = new UserRequest();
        loginInDTO.setUsername("cos");
        loginInDTO.setPassword("1234");

        // stub
        User cos = newMockUser(1L, "cos", "코스");
        MyUserDetails myUserDetails = new MyUserDetails(cos);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                myUserDetails, myUserDetails.getPassword(), myUserDetails.getAuthorities()
        );
        Mockito.when(authenticationManager.authenticate(any())).thenReturn(authentication);

        // when


        // then
        Assertions.assertThat(userService.로그인(loginInDTO)).isTrue();
    }

    @Test
    public void 유저상세보기_test() throws Exception{
        // given
        Long id = 1L;

        // stub
        User cos = newMockUser(1L, "cos", "코스");
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(cos));

        // when
        UserResponse.DetailOutDTO detailOutDTO  = userService.회원상세보기(id);

        // then
        Assertions.assertThat(detailOutDTO.getId()).isEqualTo(1L);
        Assertions.assertThat(detailOutDTO.getUsername()).isEqualTo("cos");
        Assertions.assertThat(detailOutDTO.getEmail()).isEqualTo("cos@nate.com");
        Assertions.assertThat(detailOutDTO.getFullName()).isEqualTo("코스");
        Assertions.assertThat(detailOutDTO.getRole()).isEqualTo("USER");
    }
}
