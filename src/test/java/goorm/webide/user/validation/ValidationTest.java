package goorm.webide.user.validation;

import goorm.webide.user.dto.request.RegisterRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ValidationTest {
    @Test
    @DisplayName("회원가입 요청 Validation 테스트")
    void 회원가입_요청_Validation_테스트(){
        // 검증기
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("test123")
                .password("test123")
                .passwordConfirm("test1234@#")
                .email("test123@gmail.com")
                .nickname("lee")
                .build();

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);
        for (ConstraintViolation<RegisterRequest> violation : violations) {
            System.out.println("violation = " + violation);
            System.out.println("violation.getMessage() = " + violation.getMessage());
        }
    }
}
