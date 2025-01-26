package me.geon.artice;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    public static final ApiResponse<String> SUCCESS = success("");

    private int code;

    private String message;

    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(0, "", data);
    }

    public static ApiResponse<Object> error(Integer errorCode) {
        return new ApiResponse<>(errorCode, "", null);
    }

    public static ApiResponse<Object> error(Integer errorCode, String message) {
        return new ApiResponse<>(errorCode, message, null);
    }
}
