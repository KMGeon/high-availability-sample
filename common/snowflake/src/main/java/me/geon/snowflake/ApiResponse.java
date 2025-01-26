package me.geon.snowflake;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@NoArgsConstructor
public class ApiResponse<L, R> {
    private int status;
    private L left;  // 에러 응답용
    private R right; // 성공 응답용

    // 성공과 실패를 구분하는 플래그 추가
    private boolean isSuccess;

    // 모든 필드를 포함하는 생성자
    public ApiResponse(int status, L left, R right, boolean isSuccess) {
        this.status = status;
        this.left = left;
        this.right = right;
        this.isSuccess = isSuccess;
    }

    // 성공 응답용 팩토리 메서드
    public static <L, R> ApiResponse<L, R> success(int statusCode, R data) {
        return new ApiResponse<>(statusCode, null, data, true);
    }

    // 실패 응답용 팩토리 메서드
    public static <L, R> ApiResponse<L, R> error(int statusCode, L error) {
        return new ApiResponse<>(statusCode, error, null, false);
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public boolean isError() {
        return !isSuccess;
    }
}