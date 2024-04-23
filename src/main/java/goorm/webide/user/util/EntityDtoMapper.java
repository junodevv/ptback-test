package goorm.webide.user.util;

import goorm.webide.user.dto.UserDto;
import goorm.webide.user.dto.request.RegisterRequest;
import goorm.webide.user.dto.response.UserResponse;
import goorm.webide.user.entity.User;
import goorm.webide.user.util.enums.RegisterResult;

public class EntityDtoMapper {
    /**
     * 회원가입 요청 -> User Entity
     * @param registerRequest
     * @return User
     */
    public static User registerReqToUser(RegisterRequest registerRequest){
        return User.builder()
                .username(registerRequest.getUsername())
                .password(registerRequest.getPassword())
                .email(registerRequest.getEmail())
                .nickname(registerRequest.getNickname())
                .build();
    }
    /**
     * User Entity -> 회원가입 응답
     * @param user
     * @return RegisterResponse
     */
    public static UserResponse userToRegisterResponse(User user){
        return UserResponse.builder()
                .success(RegisterResult.SUCCESS.getResult())
                .message(RegisterResult.SUCCESS.getMessage())
                .user(userToDto(user))
                .build();
    }
    /**
     * User Entity -> UserDto
     * @param user
     * @return UserDto
     */
    public static UserDto userToDto(User user){
        return UserDto.builder()
                .userNo(user.getUserNo())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }
}
