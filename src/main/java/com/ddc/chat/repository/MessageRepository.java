package com.ddc.chat.repository;

import com.ddc.chat.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface MessageRepository extends JpaRepository<ChatMessage, Long> {

    Page<ChatMessage> findAllByChatId (Long chatId, Pageable pageable);

    Page<ChatMessage> findAllByChatIdAndSender (Long chatId, String sender, Pageable pageable);

    Page<ChatMessage> findAllByChatIdAndDate (Long chatId, LocalDate createdAt, Pageable pageable);

}
