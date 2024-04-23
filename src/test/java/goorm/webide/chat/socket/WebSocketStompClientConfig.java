//package goorm.webide.chat.socket;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.web.socket.client.standard.StandardWebSocketClient;
//import org.springframework.web.socket.messaging.WebSocketStompClient;
//import org.springframework.web.socket.sockjs.client.SockJsClient;
//import org.springframework.web.socket.sockjs.client.Transport;
//import org.springframework.web.socket.sockjs.client.WebSocketTransport;
//
//import java.util.Collections;
//import java.util.List;
//
//@Profile("test")
//@Configuration
//public class WebSocketStompClientConfig {
//    @Bean
//    public WebSocketStompClient stompClient() {
//        return new WebSocketStompClient(new SockJsClient(createTransportClient()));
//    }
//
//    private List<Transport> createTransportClient() {
//        return Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()));
//    }
//}
