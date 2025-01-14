package com.ddc.chat.service;

import com.ddc.chat.controller.request.CreateMessageRequest;
import com.ddc.chat.controller.request.UpdateMessageRequest;
import com.ddc.chat.controller.response.MessageResponse;
import com.ddc.chat.entity.ChatMessage;

import java.util.List;

public interface MessageService {

    ChatMessage create(CreateMessageRequest createChatMessageRequest, Long chatId);

    List<MessageResponse> findAllByChatId(Long chatId);

    List<MessageResponse> findAllByChatIdAndSender(Long chatId, String sender);

    Long update(Long id, UpdateMessageRequest updateMessageRequest);

    void delete(Long id);

}
