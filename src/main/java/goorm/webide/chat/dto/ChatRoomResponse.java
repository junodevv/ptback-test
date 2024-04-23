package goorm.webide.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomResponse {
    private Long userNo;
    private Long roomNo;
    private String roomName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateAt;

    public ChatRoomResponse(Long roomNo, String roomName, LocalDateTime createdAt, LocalDateTime updateAt) {
        this.roomNo = roomNo;
        this.roomName = roomName;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }
}
