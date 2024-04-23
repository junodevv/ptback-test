package goorm.webide.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import goorm.webide.user.dto.UserDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private Boolean success;
    private String message;

    @JsonInclude(Include.NON_EMPTY)
    private UserDto user;
    @JsonInclude(Include.NON_EMPTY)
    private List<String> errors;
}

/** 정상응답
 * {
 *     "success": true,
 *     "message": "회원가입이 성공적으로 완료되었습니다.",
 *     "user": {
 *         "userNo": "1",
 *         "email": "test123@gmail.com",
 *         "nickname": "leejuno"
 *     }
 * }
 */
/** 에러응답
 * {
 *     "success": false,
 *     "message": "회원가입에 실패했습니다.",
 *     "errors": [
 *         "요청 형식이 올바르지 않습니다."
 *         // "username은 5자 이상 20자 이하여야 합니다." 등등
 *     ]
 * }
 */