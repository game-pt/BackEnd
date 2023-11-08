package com.a405.gamept.global.error.exception;

import com.a405.gamept.game.util.exception.GameException;
import com.a405.gamept.global.error.dto.ErrorResponseDto;
import com.a405.gamept.global.error.exception.custom.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
    private final SimpMessagingTemplate webSocket;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> businessExceptionHandle(BusinessException e) {
        log.warn("businessException : {}", e);
        return ResponseEntity.internalServerError()
                .body(ErrorResponseDto.of(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> allUncaughtHandle(Exception e) {
        log.error("allUncaughtHandle : {}", e.getMessage());
        return ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> methodArgumentNotValidExceptionHandle(MethodArgumentNotValidException e) {
        log.error("methodArgumentNotValidException : {}", e.getFieldErrors().get(0).getDefaultMessage());
        return new ResponseEntity<>(e.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GameException.class)
    protected ResponseEntity<?> gameExceptionHandle(GameException e) {
        log.error("GameException : {}", e.getMessage());
        return new ResponseEntity<>((e.getCode() == HttpStatus.INTERNAL_SERVER_ERROR)? "서버 오류" : e.getMessage(), e.getCode());
    }

    @MessageExceptionHandler(GameException.class)
    @SendTo("/topic/error")
    protected String socketGameExceptionHandle (GameException e) {
        log.error("GameException : {}", e.getMessage());
        if(e.getCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
            return "서버 오류";
        } else {
            return e.getMessage();
        }
    }
}
