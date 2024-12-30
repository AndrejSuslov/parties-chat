package com.ddc.chat.service;

import com.ddc.chat.controller.request.UpdateChatRequest;
import com.ddc.chat.controller.response.ChatResponse;
import com.ddc.chat.controller.request.CreateChatRequest;
import com.ddc.chat.entity.ChatEntity;

import java.util.List;

public interface ChatService {

    Long create(CreateChatRequest createChatRequest);

    ChatEntity findById(Long id);

    List<ChatResponse> findAllByUserId(Long userId);

    List<ChatResponse> findAll();

    Long update(UpdateChatRequest updateChatRequest);

    void delete(Long chatId);
}
