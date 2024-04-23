package goorm.webide.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserDto {
    private Long userNo;
    private String email;
    private String nickname;
    /**
     * "userNo": "1",
     *  *         "email": "test123@gmail.com",
     *  *         "nickname": "leejuno"
     */
}
