package goorm.webide.chat.repository;

import goorm.webide.chat.entity.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {

    List<ChatUser> findByUserUserNo(Long userNo);

    ChatUser findByChatRoomRoomNoAndUserUserNo(Long roomNo, Long userNo);
}
