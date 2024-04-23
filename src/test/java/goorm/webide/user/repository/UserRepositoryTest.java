//package goorm.webide.user.repository;
//
//import goorm.webide.user.util.enums.DeleteStatus;
//import goorm.webide.user.entity.User;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class UserRepositoryTest {
//    @Autowired UserRepository repository;
//
//    @Test
//    @DisplayName("entity 저장 테스트")
//    void entity_저장_테스트(){
//        User user = User.builder()
//                .username("test1234")
//                .password("test123@#")
//                .email("test123@gmail.com")
//                .nickname("leejuno")
//                .build();
//
//        User user1 = repository.save(user);
//
//        System.out.println("user1 = " + user1);
//        Assertions.assertThat(user1.getCreatedAt()).isNotNull();
//        Assertions.assertThat(user1.getUpdatedAt()).isNotNull();
//        Assertions.assertThat(user1.getDeleteStatus()).isEqualTo(DeleteStatus.ACTIVE);
//    }
//}