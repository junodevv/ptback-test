package goorm.webide.user.service;

import goorm.webide.user.dto.UserDetailDto;
import goorm.webide.user.entity.User;
import goorm.webide.user.repository.UserRepository;
import goorm.webide.user.util.enums.LoginResult;
import goorm.webide.user.util.exception.NoUserDataException;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userData = repository.findUserByUsername(username);
        return userData.map(UserDetailDto::new)
                .orElseThrow(() -> new NoUserDataException( // 의미없는 처리
                        LoginResult.FAIL.getResult(), LoginResult.FAIL.getMessage(),
                        Arrays.asList("유효하지 않은 사용자 또는 비밀번호입니다.service"))
        );
    }
}
