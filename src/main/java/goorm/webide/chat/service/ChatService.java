package goorm.webide.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import goorm.webide.chat.dto.ChatApiResponse;
import goorm.webide.chat.dto.ChatRequest;
import goorm.webide.chat.dto.ChatResponse;
import goorm.webide.chat.entity.Chat;
import goorm.webide.chat.entity.ChatRoom;
import goorm.webide.chat.repository.ChatRepository;
import goorm.webide.chat.repository.ChatRoomRepository;
import goorm.webide.user.entity.User;
import goorm.webide.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    /**
     * 채팅 보내기(WebSocket + STOMP 사용) - 채팅 메시지를 저장하고 반환
     *
     * @param chatRequest 사용자 번호와 채팅 메시지 내용가 포함된 요청 객체
     * @param roomNo 채팅방 고유 번호
     * @return 생성된 채팅 메시지에 대한 응답 데이터
     * @throws JsonProcessingException JSON 처리 중 발생하는 예외
     * @throws IllegalArgumentException 채팅방 또는 사용자를 찾을 수 없을 때 발생
     */
    @Transactional
    public ChatResponse saveChat(ChatRequest chatRequest, Long roomNo) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(chatRequest);

        ChatRoom chatRoom = chatRoomRepository.findById(roomNo)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));
        User user = userRepository.findById(chatRequest.getUserNo())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Chat chat = Chat.builder()
                .chatTxt(json)
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .chatRoom(chatRoom)
                .user(user)
                .build();
        chatRepository.save(chat);

        return new ChatResponse(
                chat.getChatNo(),
                chatRoom.getRoomNo(),
                user.getUserNo(),
                chat.getChatTxt(),
                chat.getCreatedAt(),
                chat.getUpdateAt()
        );
    }

    /**
     * 채팅 불러오기 - 특정 채팅방의 채팅을 페이징하여 불러옴
     *
     * @param roomNo 채팅방 고유 번호
     * @param pageable 페이징 정보 (페이지 번호, 페이지 크기 등)
     * @return 해당 채팅방의 모든 채팅을 페이징한 응답 객체
     */
    @Transactional(readOnly = true)
    public ChatApiResponse<Page<ChatResponse>> getAllChatsByRoomNo(Long roomNo, Pageable pageable) {
        Page<Chat> chats = chatRepository.findByChatRoomRoomNo(roomNo, pageable);
        Page<ChatResponse> chatResponse =  chats.map(chat -> new ChatResponse(
                chat.getChatNo(),
                chat.getChatRoom().getRoomNo(),
                chat.getUser().getUserNo(),
                chat.getChatTxt(),
                chat.getCreatedAt(),
                chat.getUpdateAt()
        ));
        return ChatApiResponse.success(chatResponse, "채팅 내역을 성공적으로 불러왔습니다.");
    }

    /**
     * 특정 채팅방 내 채팅 메시지 검색
     *
     * @param roomNo 채팅방 고유 번호
     * @param searchTxt 검색할 텍스트
     * @param pageable 페이징 정보 (페이지 번호, 페이지 크기 등)
     * @return 검색된 채팅 메시지들을 페이징한 응답 객체
     */
    @Transactional(readOnly = true)
    public ChatApiResponse<Page<ChatResponse>> searchChatsByRoomNo(Long roomNo, String searchTxt, Pageable pageable) {
        Page<Chat> searchChats = chatRepository.findByChatRoomRoomNoAndChatTxtChatTxtContaining(roomNo, searchTxt, pageable);
        Page<ChatResponse> chatResponses = searchChats.map(chat -> new ChatResponse(
                chat.getChatNo(),
                chat.getChatRoom().getRoomNo(),
                chat.getUser().getUserNo(),
                chat.getChatTxt(),
                chat.getCreatedAt(),
                chat.getUpdateAt()
        ));
        return ChatApiResponse.success(chatResponses, "검색 결과입니다.");
    }
}
