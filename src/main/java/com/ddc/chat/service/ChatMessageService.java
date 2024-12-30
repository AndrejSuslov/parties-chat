package com.ddc.chat.service;

import com.ddc.chat.controller.request.CreateChatMessageRequest;
import com.ddc.chat.controller.request.UpdateMessageRequest;
import com.ddc.chat.controller.response.ChatMessageResponse;
import com.ddc.chat.entity.ChatMessage;

import java.util.List;

public interface ChatMessageService {

    ChatMessage create(CreateChatMessageRequest createChatMessageRequest);

    List<ChatMessageResponse> findAllByChatId(Long chatId);

    List<ChatMessageResponse> findAllByChatIdAndSender(Long chatId, String sender);

    Long update(Long id, UpdateMessageRequest updateMessageRequest);

    void delete(Long id);

}
