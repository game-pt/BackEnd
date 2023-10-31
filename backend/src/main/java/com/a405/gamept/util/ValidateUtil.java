package com.a405.gamept.util;

import com.a405.gamept.game.util.exception.GameException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;

public class ValidateUtil {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    static Set<ConstraintViolation<Object>> violations;

    public static void validate(Object object) {
        violations = validator.validate(object);

        if (!violations.isEmpty()) {  // 유효성 검사 실패 시
            for (ConstraintViolation<Object> violation : violations) {
                throw new GameException(violation.getMessage());
            }
        }

        return;
    }
}
