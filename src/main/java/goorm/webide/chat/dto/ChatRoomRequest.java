package goorm.webide.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomRequest {
    @NotNull
    private Long userNo;

    @NotBlank(message = "채팅방 이름을 입력해야 합니다.")
    @Size(max = 20, message = "채팅방 이름은 최대 20자까지 가능합니다.")
    private String roomName;
}
