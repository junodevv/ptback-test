package goorm.webide.user.util.exception;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class RegisterException extends UserException{
    public RegisterException(Boolean success, String message, List<String> errors) {
        super(success, message, errors);
    }
}
