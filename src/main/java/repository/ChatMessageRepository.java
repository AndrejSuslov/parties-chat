package repository;

import entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findAllByChatId (Long chatId);

    List<ChatMessage> findAllByChatIdAndSender (Long chatId, String sender);

}
