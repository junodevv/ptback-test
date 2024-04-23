package goorm.webide.user.util.exception.handler;

import goorm.webide.user.dto.response.UserResponse;
import goorm.webide.user.util.exception.DuplicateException;
import goorm.webide.user.util.exception.NoUserDataException;
import goorm.webide.user.util.exception.RegisterException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "goorm.webide.user")
public class UserExHandler {
    // TODO: 중복 코드들을 하나로 만들도록 리팩토링해보기
    // 400 쟐못된 요청
    @ExceptionHandler(RegisterException.class)
    public ResponseEntity registerExHandler(RegisterException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(UserResponse.builder()
                        .success(e.getSuccess())
                        .message(e.getMessage())
                        .errors(e.getErrors())
                        .build());
    }
    // 409 중복
    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity duplicateExHandler(DuplicateException e){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(UserResponse.builder()
                        .success(e.getSuccess())
                        .message(e.getMessage())
                        .errors(e.getErrors())
                        .build());
    }
    // 401 미인증 사용자, 유효하지 않은 사용자
    @ExceptionHandler(NoUserDataException.class)
    public ResponseEntity NoUserdataExHandler(NoUserDataException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(UserResponse.builder()
                        .success(e.getSuccess())
                        .message(e.getMessage())
                        .errors(e.getErrors())
                        .build());
    }
}
