package goorm.webide.chat.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"success", "message","data"})
public class ChatApiResponse<T> implements Serializable {

    private boolean success;
    private String message;
    private T data;

    // 성공 응답 생성
    public static <T> ChatApiResponse<T> success(T data, String message) {
        return new ChatApiResponse<>(true, message, data);
    }

    // 실패 응답 생성
    public static <T> ChatApiResponse<T> fail(String message) {
        return new ChatApiResponse<>(false, message, null);
    }
}
