package com.a405.gamept.game.util.exception;

import com.a405.gamept.game.util.enums.GameErrorMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GameException extends RuntimeException {
    private final HttpStatus code;

    public GameException(GameErrorMessage gameErrorMessage) {
        super(gameErrorMessage.getMessage());
        this.code = gameErrorMessage.getCode();
    }

    public GameException(String message) {
        super(message);
        this.code = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
