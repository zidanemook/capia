package com.game.capia.dto.user;

import lombok.Getter;
import lombok.Setter;
import com.game.capia.model.user.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserRequest {

    private String type;

    @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$", message = "영문/숫자 2~20자 이내로 작성해주세요")
    @NotEmpty
    private String username;
    @NotEmpty
    @Size(min = 4, max = 20)
    private String password;

    public boolean isLoginRequest() {
        return "LOGIN".equalsIgnoreCase(type);
    }

    public boolean isJoinRequest() {
        return "JOIN".equalsIgnoreCase(type);
    }

    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .build();
    }
}
