package goorm.webide.chat.controller;

import goorm.webide.chat.dto.ChatRoomRequest;
import goorm.webide.chat.dto.ChatRoomResponse;
import goorm.webide.chat.dto.ChatApiResponse;
import goorm.webide.chat.service.ChatRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat/rooms")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    /** 채팅방 생성 POST /chat/rooms */
    @PostMapping
    public ResponseEntity<ChatApiResponse<ChatRoomResponse>> createChatRoom(
            @Valid @RequestBody ChatRoomRequest roomRequest
    ) {
        ChatApiResponse<ChatRoomResponse> apiResponse = chatRoomService.createChatRoom(roomRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    /** 전체 채팅방 목록 조회 GET /chat/rooms */
    @GetMapping
    public ResponseEntity<ChatApiResponse<List<ChatRoomResponse>>> getAllRooms() {
        ChatApiResponse<List<ChatRoomResponse>> apiResponse = chatRoomService.findAllRooms();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    /** 회원별 채팅방 목록 조회 GET /chat/rooms/user?userNo= */
    @GetMapping("/user")
    public ResponseEntity<ChatApiResponse<List<ChatRoomResponse>>> getAllRoomsByUserNo(
            @RequestParam("userNo") Long userNo
    ) {
        ChatApiResponse<List<ChatRoomResponse>> apiResponse = chatRoomService.findAllRoomsByUserId(userNo);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    /** 채팅방 삭제 DELETE /chat/rooms/{roomNo}?userNo= */
    @DeleteMapping("/{roomNo}")
    public ResponseEntity<ChatApiResponse<Long>> deleteRoomByRoomNo(
            @PathVariable("roomNo") Long roomNo,
            @RequestParam("userNo") Long userNo
    ) {
        ChatApiResponse<Long> apiResponse = chatRoomService.deleteRoomByRoomNo(roomNo, userNo);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
