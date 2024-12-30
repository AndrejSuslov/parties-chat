package com.ddc.chat.service.impl;

import com.ddc.chat.controller.request.CreateChatRequest;
import com.ddc.chat.controller.request.UpdateChatRequest;
import com.ddc.chat.controller.response.ChatResponse;
import com.ddc.chat.entity.ChatEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ddc.chat.repository.ChatRepository;
import com.ddc.chat.service.ChatService;
import com.ddc.chat.service.mapper.ChatMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository repository;
    private final ChatMapper mapper;

    @Override
    public Long create(CreateChatRequest createChatRequest) {
        final ChatEntity entity = mapper.toEntity(createChatRequest);
        return repository.save(entity).getId();
    }

    @Override
    public ChatEntity findById(Long id) {
        return repository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public List<ChatResponse> findAllByUserId(Long userId) {
        final List<ChatEntity> allByUserId = repository.findAllByUserId(userId);
        return mapper.toResponses(allByUserId);
    }

    @Override
    public List<ChatResponse> findAll() {
        final List<ChatEntity> all = repository.findAll();
        return mapper.toResponses(all);
    }

    @Override
    public Long update(UpdateChatRequest updateChatRequest) {
        final ChatEntity entity = mapper.toEntity(updateChatRequest);
        return repository.save(entity).getId();
    }

    @Override
    public void delete(Long chatId) {
        repository.deleteById(chatId);
    }

}
