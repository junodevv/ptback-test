//package goorm.webide.user.service;
//
//import goorm.webide.user.dto.request.RegisterRequest;
//import goorm.webide.user.dto.response.UserResponse;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class UserServiceTest {
//    @Autowired UserService service;
//
//    @Test
//    @DisplayName("회원가입_유저등록_테스트")
//    void 회원가입_유저등록_테스트(){
//        RegisterRequest registerRequest = RegisterRequest.builder()
//                .username("tester1234")
//                .password("test1234@#")
//                .passwordConfirm("test1234@#")
//                .email("tester1234@gmail.com")
//                .nickname("leejuno")
//                .build();
//
//        UserResponse userResponse = service.registerUser(registerRequest);
//
//        Assertions.assertThat(userResponse.getUser().getNickname())
//                .isEqualTo(registerRequest.getNickname());
//    }
//}