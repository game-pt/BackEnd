package com.a405.gamept.util;

import com.a405.gamept.game.util.exception.GameException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidateUtil {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private static Set<ConstraintViolation<Object>> violations;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static void validate(Object object) {
        violations = validator.validate(object);

        if (!violations.isEmpty()) {  // 유효성 검사 실패 시
            for (ConstraintViolation<Object> violation : violations) {
                throw new GameException(violation.getMessage());
            }
        }
    }

    public static String getRandomUID() {
        return getRandomUID(6);  // 기본 길이를 6으로 설정
    }

    public static String getRandomUID(int streamSize) {
        return new Random().ints(6, 0, CHARACTERS.length())
                .mapToObj(CHARACTERS::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}
