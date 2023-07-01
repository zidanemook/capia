package com.game.capia.dto.user;

import lombok.Getter;
import lombok.Setter;
import com.game.capia.model.user.User;

public class UserResponse {
    @Getter @Setter
    public static class DetailOutDTO{
        private Long id;
        private String username;
        private String email;
        private String fullName;
        private String role;

        public DetailOutDTO(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
        }
    }

    @Setter
    @Getter
    public static class JoinOutDTO {
        private Long id;
        private String username;
        private String fullName;

        public JoinOutDTO(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
        }
    }
}
