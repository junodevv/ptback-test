package goorm.webide.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {
    @NotNull
    private Long userNo;

    @NotBlank(message = "채팅 메시지를 입력해야 합니다.")
    private String chatTxt;
}
