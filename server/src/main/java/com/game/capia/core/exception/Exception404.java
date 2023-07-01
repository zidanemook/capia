package com.game.capia.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import com.game.capia.dto.ResponseDTO;


// 권한 없음
@Getter
public class Exception404 extends RuntimeException {
    public Exception404(String message) {
        super(message);
    }

    public ResponseDTO<?> body(){
        return new ResponseDTO<>(HttpStatus.NOT_FOUND, "notFound", getMessage());
    }

    public HttpStatus status(){
        return HttpStatus.NOT_FOUND;
    }
}