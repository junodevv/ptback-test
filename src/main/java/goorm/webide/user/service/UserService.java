package goorm.webide.user.service;

import goorm.webide.user.dto.request.RegisterRequest;
import goorm.webide.user.dto.response.UserResponse;
import goorm.webide.user.entity.User;
import goorm.webide.user.repository.UserRepository;
import goorm.webide.user.util.EntityDtoMapper;
import goorm.webide.user.util.enums.RegisterResult;
import goorm.webide.user.util.exception.DuplicateException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse registerUser(RegisterRequest registerRequest){
        checkDuplicate(registerRequest.getEmail(), registerRequest.getUsername());
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        registerRequest.setPassword(encodedPassword);
        User user = EntityDtoMapper.registerReqToUser(registerRequest);
        User saveResult = repository.save(user);
        // 성공
        return EntityDtoMapper.userToRegisterResponse(saveResult);
    }

    private void checkDuplicate(String email, String username){
        List<String> errors = new ArrayList<>();
        if(repository.findUserByEmail(email).isPresent()){
            errors.add("이미 사용중인 이메일입니다.");
        }
        if (repository.findUserByUsername(username).isPresent()) {
            errors.add("이미 사용중인 아이디입니다.");
        }
        if(errors.size()>0){
            throw new DuplicateException(RegisterResult.FAIL.getResult(), RegisterResult.FAIL.getMessage(), errors);
        }
    }
}
