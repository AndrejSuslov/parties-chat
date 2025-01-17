package com.ddc.chat.service.impl;

import com.ddc.chat.controller.request.CreateChatRequest;
import com.ddc.chat.controller.request.UpdateChatRequest;
import com.ddc.chat.controller.response.ChatResponse;
import com.ddc.chat.entity.ChatEntity;
import com.ddc.chat.enums.ChatType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.ddc.chat.repository.ChatRepository;
import com.ddc.chat.service.ChatService;
import com.ddc.chat.service.mapper.ChatMapper;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository repository;
    private final ChatMapper mapper;

    @Override
    public Long create(CreateChatRequest createChatRequest) {
        if(Objects.equals(createChatRequest.getType(), ChatType.PRIVATE.toString()) && createChatRequest.getUserIds().size() != 2) {
            throw new RuntimeException("In private chat cannot be more than 2 users");
        }
        final ChatEntity entity = mapper.toEntity(createChatRequest);
        return repository.save(entity).getId();
    }

    @Override
    public ChatResponse findById(Long id) {
        final ChatEntity chat = repository.findById(id).orElseThrow(RuntimeException::new);
        return mapper.toResponse(chat);
    }

    @Override
    public ChatResponse findByName(String name) {
        ChatEntity chat = repository.findByName(name);
        return mapper.toResponse(chat);
    }

    @Override
    public List<ChatResponse> findAllByUserId(Long userId) {
        final List<ChatEntity> allByUserId = repository.findAllByUserId(userId);
        return mapper.toResponses(allByUserId);
    }

    @Override
    public List<ChatResponse> findAllByUserIdAndType(Long userId, ChatType type) {
        final List<ChatEntity> allByUserId = repository.findAllByUserIdAndType(userId, type.toString());
        return mapper.toResponses(allByUserId);
    }

    @Override
    public Page<ChatResponse> findAll(Pageable pageable) {
        final Page<ChatEntity> all = repository.findAll(pageable);
        return all.map(mapper::toResponse);
    }

    @Override
    public Long update(UpdateChatRequest updateChatRequest, Long id) {
        final ChatEntity entity = mapper.toEntity(updateChatRequest);
        entity.setId(id);
        return repository.save(entity).getId();
    }

    @Override
    public void delete(Long chatId) {
        repository.deleteById(chatId);
    }

}
