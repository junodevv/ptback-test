package goorm.webide.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private Long chatNo;
    private Long roomNo;
    private Long userNo;
    private String chatTxt;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
