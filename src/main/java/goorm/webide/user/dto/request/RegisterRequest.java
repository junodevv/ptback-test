package goorm.webide.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
public class RegisterRequest {
    @NotBlank(message = "아이디를 입력해주세요")
    @Pattern(
            message = "잘못된 아이디 형식입니다. 영문자, 숫자를 조합한 5자~20자를 입력해주세요.",
            regexp = "^[a-zA-Z0-9]{5,20}$"
    )
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Pattern(
            message = "잘못된 비밀번호 형식입니다. 영문자, 숫자, 특수문자를 모두 1개 이상 조합한 8자~20자를 입력해주세요.",
            regexp = "^(?=.*[~!@#$%^&*()_+`\\-=\\[\\]\\{\\};':\\\",./<>?])"
                    + "(?=.*[0-9])"
                    + "(?=.*[a-zA-Z])"
                    + "\\S{8,20}$"
    )
    private String password;

    @JsonProperty("password_confirmation")
    @NotBlank(message = "비밀번호 확인을 입력해주세요")
    private String passwordConfirm; // 이건 비밀번호와 똑같은지만 확인

    @NotBlank(message = "이메일을 입력해주세요")
    @Email
    private String email;

    @NotBlank(message = "닉네임을 입력해주세요")
    @Length(min = 2, max = 20)
    private String nickname;
}
