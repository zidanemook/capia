package com.game.capia.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import com.game.capia.core.annotation.MyErrorLog;
import com.game.capia.core.annotation.MyLog;
import com.game.capia.core.auth.session.MyUserDetails;
import com.game.capia.core.exception.Exception403;
import com.game.capia.dto.ResponseDTO;
import com.game.capia.dto.user.UserRequest;
import com.game.capia.dto.user.UserResponse;
import com.game.capia.service.UserService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class UserController {

    //private final UserService userService;

//    @MyErrorLog
//    @MyLog
//    @PostMapping("/join")
//    public ResponseEntity<?> join(@RequestBody @Valid UserRequest.JoinInDTO joinInDTO, Errors errors) {
//        UserResponse.JoinOutDTO joinOutDTO = userService.회원가입(joinInDTO);
//        ResponseDTO<?> responseDTO = new ResponseDTO<>(joinOutDTO);
//        return ResponseEntity.ok(responseDTO);
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody UserRequest.LoginInDTO loginInDTO){
//        userService.로그인(loginInDTO);
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping("/s/user/{id}")
//    public ResponseEntity<?> detail(@PathVariable Long id, @AuthenticationPrincipal MyUserDetails myUserDetails) throws JsonProcessingException {
//        if(id.longValue() != myUserDetails.getUser().getId()){
//            throw new Exception403("권한이 없습니다");
//        }
//        UserResponse.DetailOutDTO detailOutDTO = userService.회원상세보기(id);
//        //System.out.println(new ObjectMapper().writeValueAsString(detailOutDTO));
//        ResponseDTO<?> responseDTO = new ResponseDTO<>(detailOutDTO);
//        return ResponseEntity.ok(responseDTO);
//    }
}
