package goorm.webide.chat.service;

import goorm.webide.chat.dto.ChatApiResponse;
import goorm.webide.chat.dto.ChatRoomRequest;
import goorm.webide.chat.dto.ChatRoomResponse;
import goorm.webide.chat.entity.ChatRoom;
import goorm.webide.chat.entity.ChatUser;
import goorm.webide.chat.repository.ChatRoomRepository;
import goorm.webide.chat.repository.ChatUserRepository;
import goorm.webide.user.entity.User;
import goorm.webide.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatUserRepository chatUserRepository;

    /**
     * 채팅방 생성
     *
     * @param roomRequest 요청 객체(사용자 번호, 방 이름)
     * @return 생성된 채팅방의 정보에 대한 응답 객체
     * @throws RuntimeException 사용자 번호가 유효하지 않을 경우 예외 발생
     */
    @Transactional
    public ChatApiResponse<ChatRoomResponse> createChatRoom(ChatRoomRequest roomRequest) {
        User user = userRepository.findById(roomRequest.getUserNo())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        ChatRoom chatRoom = ChatRoom.builder()
                .roomName(roomRequest.getRoomName())
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
        chatRoomRepository.save(chatRoom);

        ChatUser chatUser = ChatUser.builder()
                .chatRoom(chatRoom)
                .user(user)
                .build();
        chatUserRepository.save(chatUser);

        ChatRoomResponse roomResponse = new ChatRoomResponse(
                roomRequest.getUserNo(),
                chatRoom.getRoomNo(),
                chatRoom.getRoomName(),
                chatRoom.getCreatedAt(),
                chatRoom.getUpdateAt());
        return ChatApiResponse.success(roomResponse, "채팅방이 생성되었습니다.");
    }

    /**
     * 모든 채팅방 목록 조회
     *
     * @return 채팅방 목록에 대한 응답 객체
     */
    @Transactional(readOnly = true)
    public ChatApiResponse<List<ChatRoomResponse>> findAllRooms() {
         List<ChatRoomResponse> roomResponse = chatRoomRepository.findAll().stream()
                .map(room -> new ChatRoomResponse(
                        room.getRoomNo(),
                        room.getRoomName(),
                        room.getCreatedAt(),
                        room.getUpdateAt()))
                .toList();
        return ChatApiResponse.success(roomResponse, "전체 채팅방 목록 조회에 성공했습니다.");
    }

    /**
     * 회원별 채팅방 목록 조회
     *
     * @param userNo 사용자 고유 번호
     * @return 해당 사용자의 채팅방 목록에 대한 응답 객체
     */
    @Transactional(readOnly = true)
    public ChatApiResponse<List<ChatRoomResponse>> findAllRoomsByUserId(Long userNo) {
        List<ChatRoomResponse> roomResponse = chatUserRepository.findByUserUserNo(userNo).stream()
                .map(chatUser -> new ChatRoomResponse(
                        chatUser.getUser().getUserNo(),
                        chatUser.getChatRoom().getRoomNo(),
                        chatUser.getChatRoom().getRoomName(),
                        chatUser.getChatRoom().getCreatedAt(),
                        chatUser.getChatRoom().getUpdateAt()))
                .toList();
        return ChatApiResponse.success(roomResponse, "채팅방 목록 조회에 성공했습니다.");
    }

    /**
     * 채팅방을 삭제 - 해당 채팅방 생성자만 삭제 가능
     *
     * @param roomNo 채팅방 고유 번호
     * @param userNo 사용자 고유 번호
     * @return 삭제 성공 여부에 대한 응답 객체
     * @throws RuntimeException 채팅방 또는 사용자를 찾을 수 없을 경우 예외 발생
     * @throws IllegalStateException 삭제 권한이 없는 경우 예외 발생
     */
    @Transactional
    public ChatApiResponse<Long> deleteRoomByRoomNo(Long roomNo, Long userNo) {
        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        ChatRoom chatRoom = chatRoomRepository.findById(roomNo)
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));

        ChatUser chatUser = chatUserRepository.findByChatRoomRoomNoAndUserUserNo(roomNo, userNo);
        if (chatUser == null) {
            throw new IllegalStateException("채팅방 삭제 권한이 없습니다.");
        }

        chatRoomRepository.deleteById(roomNo);
        return ChatApiResponse.success(null, "채팅방이 성공적으로 삭제되었습니다.");
    }
}
