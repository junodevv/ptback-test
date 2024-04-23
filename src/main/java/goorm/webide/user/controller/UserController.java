package goorm.webide.user.controller;

import goorm.webide.user.dto.request.RegisterRequest;
import goorm.webide.user.service.UserService;
import goorm.webide.user.util.enums.RegisterResult;
import goorm.webide.user.util.exception.RegisterException;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/user/register")
    public ResponseEntity registerUser(@Valid @ModelAttribute RegisterRequest registerRequest,
                                       BindingResult bindingResult
    ){
        validateAllParam(passwordValidate(registerRequest, bindingResult));
        return ResponseEntity.ok(service.registerUser(registerRequest));
    }
    @GetMapping("/test")
    public ResponseEntity test(){
        return ResponseEntity.ok("테스트 성공");
    }

    private BindingResult passwordValidate(RegisterRequest registerRequest, BindingResult bindingResult){
        if(!registerRequest.getPassword().equals(registerRequest.getPasswordConfirm())){
            bindingResult.reject("passwordMismatch", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }
        return bindingResult;
    }
    private void validateAllParam(BindingResult passwordValidateResult){
        if(passwordValidateResult.hasErrors()){
            List<String> errors = passwordValidateResult.getAllErrors().stream()
                    .map(e -> e.getDefaultMessage())
                    .toList();
            throw new RegisterException(
                    RegisterResult.FAIL.getResult(),RegisterResult.FAIL.getMessage(), errors
            );
        }
    }
}
