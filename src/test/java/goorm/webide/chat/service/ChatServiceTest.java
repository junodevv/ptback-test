//package goorm.webide.chat.service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import goorm.webide.chat.dto.ChatApiResponse;
//import goorm.webide.chat.dto.ChatRequest;
//import goorm.webide.chat.dto.ChatResponse;
//import goorm.webide.chat.dto.ChatRoomRequest;
//import goorm.webide.chat.entity.Chat;
//import goorm.webide.chat.entity.ChatRoom;
//import goorm.webide.chat.repository.ChatRepository;
//import goorm.webide.chat.repository.ChatRoomRepository;
//import goorm.webide.user.entity.User;
//import goorm.webide.user.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//class ChatServiceTest {
//    @Autowired
//    private ChatService chatService;
//    @MockBean
//    private ChatRepository chatRepository;
//    @MockBean
//    private ChatRoomRepository chatRoomRepository;
//    @MockBean
//    private UserRepository userRepository;
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    public void beforeEach() {
//        objectMapper = new ObjectMapper();
//    }
//
//    /* 채팅 보내기 */
//    @Test
//    @Transactional
//    public void saveChatTest() throws Exception {
//        // given
//        Long userNo = 1L;
//        ChatRoomRequest roomRequest = new ChatRoomRequest(userNo, "test room");
//        ChatRoom chatRoom = ChatRoom.builder()
//                .roomName(roomRequest.getRoomName())
//                .createdAt(LocalDateTime.now())
//                .updateAt(LocalDateTime.now())
//                .build();
//        User user = Mockito.mock(User.class);
//
//        ChatRequest chatRequest = new ChatRequest(userNo, "test chat text");
//        String json = objectMapper.writeValueAsString(chatRequest);
//        Chat chat = Chat.builder()
//                .chatTxt(json)
//                .createdAt(LocalDateTime.now())
//                .updateAt(LocalDateTime.now())
//                .chatRoom(chatRoom)
//                .user(user)
//                .build();
//
//        when(chatRoomRepository.findById(chatRoom.getRoomNo())).thenReturn(Optional.of(chatRoom));
//        when(userRepository.findById(chatRequest.getUserNo())).thenReturn(Optional.of(user));
//        when(chatRepository.save(any(Chat.class))).thenReturn(chat);
//
//        // when
//        ChatResponse chatResponse = chatService.saveChat(chatRequest, chatRoom.getRoomNo());
//
//        // then
//        assertNotNull(chatResponse);
//        String parseTxt = objectMapper.readTree(chatResponse.getChatTxt()).get("chatTxt").asText();
//        assertEquals("test chat text", parseTxt);
//        verify(chatRoomRepository).findById(chatRoom.getRoomNo());
//        verify(userRepository).findById(chatRequest.getUserNo());
//        verify(chatRepository).save(any(Chat.class));
//    }
//
//    /* 채팅 불러오기 */
//    @Test
//    @Transactional
//    public void getAllChatsByRoomNoTest() {
//        // given
//        Long roomNo = 1L;
//        Pageable pageable = PageRequest.of(0, 10);
//        ChatRoom chatRoom = Mockito.mock(ChatRoom.class);
//        User user = Mockito.mock(User.class);
//
//        Chat chat1 = new Chat(1L, "채팅1", LocalDateTime.now(), LocalDateTime.now(), chatRoom, user);
//        Chat chat2 = new Chat(2L, "채팅2", LocalDateTime.now(), LocalDateTime.now(), chatRoom, user);
//
//        List<Chat> chatList = Arrays.asList(chat1, chat2);
//        Page<Chat> chatPage = new PageImpl<>(chatList, pageable, chatList.size());
//        when(chatRepository.findByChatRoomRoomNo(roomNo, pageable)).thenReturn(chatPage);
//
//        // when
//        ChatApiResponse<Page<ChatResponse>> responses = chatService.getAllChatsByRoomNo(roomNo, pageable);
//
//        // then
//        assertNotNull(responses);
//        assertEquals(2, responses.getData().getTotalElements());
//        assertEquals("채팅1", responses.getData().getContent().get(0).getChatTxt());
//        assertEquals("채팅2", responses.getData().getContent().get(1).getChatTxt());
//        verify(chatRepository).findByChatRoomRoomNo(roomNo, pageable);
//    }
//}