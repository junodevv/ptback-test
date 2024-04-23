package goorm.webide.chat.socket;

import goorm.webide.chat.dto.ChatApiResponse;
import goorm.webide.chat.dto.ChatRequest;
import goorm.webide.chat.dto.ChatResponse;
import goorm.webide.chat.service.ChatService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class StompWebSocketControllerTest {

    @Autowired
    private WebSocketStompClient stompClient;
    @MockBean
    private ChatService chatService;
    @LocalServerPort
    private int port;

    private CompletableFuture<ChatApiResponse<ChatResponse>> completableFuture;
    private StompSession session;

    @BeforeEach
    public void beforeEach() throws Exception {
        this.stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        completableFuture = new CompletableFuture<>();

        session = stompClient
                .connect("ws://localhost:" + port + "/sendChat", new StompSessionHandlerAdapter() {})
                .get(10, TimeUnit.SECONDS);
    }

    @AfterEach
    public void afterEach() {
        if (session != null) {
            try {
                session.disconnect();
            } catch (Exception e) {
                System.err.println("WebSocket 세션 연결을 끊는 중 오류 발생: " + e.getMessage());
            }
        }
        if (stompClient != null) {
            stompClient.stop();
        }
    }

    private List<Transport> createTransportClient() {
        return Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()));
    }

    /* 채팅 보내기 */
//    @Test
//    public void sendChatTest() throws Exception {
//        // given
//        Long roomNo = 1L;
//        Long userNo = 1L;
//        String chatTxt = "안녕하세요!";
//
//        ChatRequest chatRequest = new ChatRequest(userNo, chatTxt);
//        ChatResponse chatResponse = new ChatResponse(userNo, roomNo, 1L, chatTxt, LocalDateTime.now(), LocalDateTime.now());
//        when(chatService.saveChat(any(ChatRequest.class), anyLong())).thenReturn(chatResponse);
//
//        session.subscribe("/topic/chat/1", new StompFrameHandler() {
//            @Override
//            public Type getPayloadType(StompHeaders headers) {
//                return ChatResponse.class;
//            }
//
//            @Override
//            public void handleFrame(StompHeaders headers, Object payload) {
//                completableFuture.complete((ChatApiResponse<ChatResponse>) payload);
//            }
//        });
//
//        // when
//        session.send("/app/sendChat/1", chatRequest);
//
//        // then
//        try {
//            ChatApiResponse<ChatResponse> response = completableFuture.get(15, TimeUnit.SECONDS);
//            assertNotNull(response);
//            assertTrue(response.isSuccess());
//            assertEquals("안녕하세요!", response.getData().getChatTxt());
//            verify(chatService).saveChat(any(ChatRequest.class), eq(1L));
//        } catch (TimeoutException e) {
//            ChatApiResponse.fail("응답 시간 초과: 시간 초과로 테스트 실패");
//        }
//    }
}