package com.a405.gamept.global.dto_response;

public record BaseResponseDto<T>(String message, T data) {

    public static <T> BaseResponseDto<T> ok(T data){
        return new BaseResponseDto<>("success",data);
    }
    public static <T> BaseResponseDto<T> error(T data){
        return new BaseResponseDto<>("error",data);
    }

    public static <T> BaseResponseDto<T> message(T data, String message){
        return new BaseResponseDto<>(message, data);
    }
}