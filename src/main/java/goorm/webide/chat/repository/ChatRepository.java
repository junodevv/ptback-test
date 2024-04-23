package goorm.webide.chat.repository;

import goorm.webide.chat.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    Page<Chat> findByChatRoomRoomNo(Long roomNo, Pageable pageable);

    @Query(value = "SELECT * FROM Chat WHERE room_no = :roomNo " +
            "AND JSON_UNQUOTE(JSON_EXTRACT(chat_txt, '$.chatTxt')) " +
            "LIKE %:searchTxt%", nativeQuery = true)
    Page<Chat> findByChatRoomRoomNoAndChatTxtChatTxtContaining(
            @Param("roomNo") Long roomNo,
            @Param("searchTxt") String searchTxt,
            Pageable pageable);
}
