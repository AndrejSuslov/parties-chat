package com.ddc.chat.repository;

import com.ddc.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findAllById (Long chatId);

    List<ChatMessage> findAllByIdAndSender (Long Id, String sender);

}
