package com.game.capia.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.game.capia.core.annotation.MyLog;
import com.game.capia.core.auth.session.MyUserDetails;
import com.game.capia.core.exception.Exception400;
import com.game.capia.core.exception.Exception401;
import com.game.capia.core.exception.Exception500;
import com.game.capia.dto.user.UserRequest;
import com.game.capia.dto.user.UserResponse;
import com.game.capia.model.user.User;
import com.game.capia.model.user.UserRepository;
import org.springframework.web.socket.WebSocketSession;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @MyLog
    @Transactional
    public UserResponse.JoinOutDTO 회원가입(UserRequest userRequest){
        Optional<User> userOP =userRepository.findByUsername(userRequest.getUsername());
        if(userOP.isPresent()){
            // 이 부분이 try catch 안에 있으면 Exception500에게 제어권을 뺏긴다.
            throw new Exception400("username", "유저네임이 존재합니다");
        }
        String encPassword = passwordEncoder.encode(userRequest.getPassword()); // 60Byte
        userRequest.setPassword(encPassword);
        System.out.println("encPassword : "+encPassword);

        // 디비 save 되는 쪽만 try catch로 처리하자.
        try {
            User userPS = userRepository.save(userRequest.toEntity());
            return new UserResponse.JoinOutDTO(userPS);
        }catch (Exception e){
            throw new Exception500("회원가입 실패 : "+e.getMessage());
        }
    }

    @MyLog
    public boolean 로그인(UserRequest userRequest) {
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
            return true;
        }catch (Exception e){
            throw new Exception401("인증되지 않았습니다");
        }
    }

    @MyLog
    public UserResponse.DetailOutDTO 회원상세보기(Long id) {
        User userPS = userRepository.findById(id).orElseThrow(
                ()-> new Exception400("id", "해당 유저를 찾을 수 없습니다")

        );
        return new UserResponse.DetailOutDTO(userPS);
    }

    public void handleRequest(WebSocketSession session, UserRequest userRequest) {
        String type = userRequest.getType();

        if ("JOIN".equals(type)) {
            회원가입(userRequest);
        } else if ("LOGIN".equals(type)) {
            로그인(userRequest);
        }
    }
}
