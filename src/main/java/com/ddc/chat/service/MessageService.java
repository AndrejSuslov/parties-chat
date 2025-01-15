package com.ddc.chat.service;

import com.ddc.chat.controller.request.CreateMessageRequest;
import com.ddc.chat.controller.request.UpdateMessageRequest;
import com.ddc.chat.controller.response.MessageResponse;
import com.ddc.chat.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {

    ChatMessage create(CreateMessageRequest createChatMessageRequest, Long chatId);

    Page<MessageResponse> findAllByChatId(Long chatId, Pageable pageable);

    Page<MessageResponse> findAllByChatIdAndSender(Long chatId, String sender, Pageable pageable);

    Page<MessageResponse> findAllByChatIdAndDate(Long chatId, String date, Pageable pageable);

    Long update(Long id, UpdateMessageRequest updateMessageRequest);

    void delete(Long id);

}
