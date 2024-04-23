package goorm.webide.user.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RegisterResult {
    FAIL(false ,"회원가입에 실패했습니다."),
    SUCCESS(true,"회원가입에 성공했습니다.");

    private final Boolean result;
    private final String message;
}
