package com.ddc.chat.service;

import com.ddc.chat.controller.request.UpdateChatRequest;
import com.ddc.chat.controller.response.ChatResponse;
import com.ddc.chat.controller.request.CreateChatRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ChatService {

    Long create(CreateChatRequest createChatRequest);

    ChatResponse findByName(String name);

    ChatResponse findById(Long id);

    List<ChatResponse> findAllByUserId(Long userId);

    Page<ChatResponse> findAll(Pageable pageable, Sort sort);

    Long update(UpdateChatRequest updateChatRequest, Long id);

    void delete(Long chatId);
}
