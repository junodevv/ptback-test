package goorm.webide.user.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoginResult {
    FAIL(false ,"로그인에 실패했습니다."),
    SUCCESS(true,"로그인에 성공했습니다.");

    private final Boolean result;
    private final String message;
}
