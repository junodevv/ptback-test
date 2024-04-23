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
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ChatRoomServiceTest {
    @Autowired
    ChatRoomService chatRoomService;
    @MockBean
    UserRepository userRepository;
    @MockBean
    ChatRoomRepository chatRoomRepository;
    @MockBean
    ChatUserRepository chatUserRepository;

    /* 채팅방 생성 */
    @Test
    @Transactional
    public void createChatRoomTest() {
        // given
        ChatRoomRequest roomRequest = new ChatRoomRequest(1L, "test room");
        ChatRoom chatRoom = ChatRoom.builder()
                .roomName(roomRequest.getRoomName())
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
        User user = Mockito.mock(User.class);
        ChatUser chatUser = ChatUser.builder()
                .chatRoom(chatRoom)
                .user(user)
                .build();

        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(chatRoom);
        when(userRepository.findById(roomRequest.getUserNo())).thenReturn(Optional.of(user));
        when(chatUserRepository.save(any(ChatUser.class))).thenReturn(chatUser);

        // when
        ChatRoomResponse roomResponse = chatRoomService.createChatRoom(roomRequest).getData();

        // then
        assertNotNull(roomResponse);
        assertEquals(roomRequest.getRoomName(), roomResponse.getRoomName());
        assertEquals(roomRequest.getUserNo(), roomResponse.getUserNo());
        verify(chatRoomRepository).save(any(ChatRoom.class));
        verify(userRepository).findById(roomRequest.getUserNo());
        verify(chatUserRepository).save(any(ChatUser.class));
    }

    /* 전체 채팅방 목록 조회 */
    @Test
    @Transactional
    public void findAllRoomsTest() {
        // given
        ChatRoomRequest roomRequest1 = new ChatRoomRequest(null, "room1");
        ChatRoom chatRoom1 = ChatRoom.builder()
                .roomName(roomRequest1.getRoomName())
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
        ChatRoomRequest roomRequest2 = new ChatRoomRequest(null, "room2");
        ChatRoom chatRoom2 = ChatRoom.builder()
                .roomName(roomRequest2.getRoomName())
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        List<ChatRoom> rooms = Arrays.asList(chatRoom1, chatRoom2);
        when(chatRoomRepository.findAll()).thenReturn(rooms);

        // when
        List<ChatRoomResponse> roomResponses = chatRoomService.findAllRooms().getData();

        // then
        assertNotNull(roomResponses);
        assertEquals(2, roomResponses.size());
        assertEquals("room1", roomResponses.get(0).getRoomName());
        assertEquals("room2", roomResponses.get(1).getRoomName());
        verify(chatRoomRepository).findAll();
    }

    /* 회원별 채팅방 목록 조회 */
    @Test
    @Transactional
    public void findAllRoomsByUserIdTest() {
        // given
        Long userNo = 1L;
        ChatRoomRequest roomRequest1 = new ChatRoomRequest(userNo, "room1");
        ChatRoom chatRoom1 = ChatRoom.builder()
                .roomName(roomRequest1.getRoomName())
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
        ChatRoomRequest roomRequest2 = new ChatRoomRequest(userNo, "room2");
        ChatRoom chatRoom2 = ChatRoom.builder()
                .roomName(roomRequest2.getRoomName())
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
        User user = Mockito.mock(User.class);

        ChatUser chatUser1 = ChatUser.builder()
                .chatRoom(chatRoom1)
                .user(user)
                .build();
        ChatUser chatUser2 = ChatUser.builder()
                .chatRoom(chatRoom2)
                .user(user)
                .build();

        List<ChatUser> chatUsers = Arrays.asList(chatUser1, chatUser2);
        when(chatUserRepository.findByUserUserNo(userNo)).thenReturn(chatUsers);

        // when
        List<ChatRoomResponse> roomResponses = chatRoomService.findAllRoomsByUserId(userNo).getData();

        // then
        assertNotNull(roomResponses);
        assertEquals(2, roomResponses.size());
        assertEquals("room1", roomResponses.get(0).getRoomName());
        assertEquals("room2", roomResponses.get(1).getRoomName());
        verify(chatUserRepository).findByUserUserNo(userNo);
    }

    /* 채팅방 삭제 DELETE /chat/rooms/{roomNo} */
    @Test
    @Transactional
    public void deleteRoomByRoomNo() {
        // given
        Long roomNo = 1L;
        Long userNo = 1L;

        User user = Mockito.mock(User.class);
        ChatRoomRequest roomRequest = new ChatRoomRequest(userNo, "test room");
        ChatRoom chatRoom = ChatRoom.builder()
                .roomName(roomRequest.getRoomName())
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
        ChatUser chatUser = ChatUser.builder()
                .chatRoom(chatRoom)
                .user(user)
                .build();

        when(userRepository.findById(userNo)).thenReturn(Optional.of(user));
        when(chatRoomRepository.findById(roomNo)).thenReturn(Optional.of(chatRoom));
        when(chatUserRepository.findByChatRoomRoomNoAndUserUserNo(roomNo, userNo)).thenReturn(chatUser);

        // when
        ChatApiResponse<Long> chatRoomResponse = chatRoomService.deleteRoomByRoomNo(roomNo, userNo);

        // then
        assertNotNull(chatRoomResponse);
        assertNull(chatRoomResponse.getData());
        assertEquals("채팅방이 성공적으로 삭제되었습니다.", chatRoomResponse.getMessage());
        verify(userRepository).findById(userNo);
        verify(chatUserRepository).findByChatRoomRoomNoAndUserUserNo(roomNo, userNo);
        verify(chatRoomRepository).deleteById(roomNo);
    }
}