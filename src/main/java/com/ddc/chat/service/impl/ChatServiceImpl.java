package com.ddc.chat.service.impl;

import com.ddc.chat.controller.request.CreateChatRequest;
import com.ddc.chat.controller.request.UpdateChatRequest;
import com.ddc.chat.controller.response.ChatResponse;
import com.ddc.chat.entity.ChatEntity;
import com.ddc.chat.enums.ChatType;
import com.ddc.chat.repository.jdbc.JdbcChatRepository;
import com.ddc.chat.util.UserIdDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    private final JdbcChatRepository jdbcRepository;

    @Override
    public Long create(CreateChatRequest createChatRequest) {
        if(Objects.equals(createChatRequest.getType(), ChatType.PRIVATE.toString()) && createChatRequest.getUserIds().size() != 2) {
            throw new RuntimeException("In private chat cannot be more than 2 users");
        }
        final ChatEntity entity = mapper.toEntity(createChatRequest);
        Long chatId = repository.save(entity).getId();
        List<Long> multiChatIds = new ArrayList<>();
        createChatRequest.getUserIds()
                .forEach(userId -> multiChatIds.add(chatId));
        jdbcRepository.saveUsers(multiChatIds, createChatRequest.getUserIds());
        return chatId;
    }

    @Override
    public ChatResponse findById(Long id) {
        final ChatEntity chat = repository.findById(id).orElseThrow(RuntimeException::new);
        ChatResponse response = mapper.toResponse(chat);
        List<ChatResponse> chatResponses = setUserIds(Collections.singletonList(response));
        return chatResponses.get(0);
    }

    @Override
    public ChatResponse findByName(String name) {
        ChatEntity chat = repository.findByName(name);
        List<Long> userIdsByName = repository.findUserIdsByName(name);
        ChatResponse response = mapper.toResponse(chat);
        response.setUserIds(userIdsByName);
        return response;
    }

    @Override
    public List<ChatResponse> findAllByUserId(Long userId) {
        final List<ChatEntity> allByUserId = repository.findAllByUserId(userId);
        List<ChatResponse> responses = mapper.toResponses(allByUserId);
        return setUserIds(responses);
    }

    @Override
    public List<ChatResponse> findAllByUserIdAndType(Long userId, ChatType type) {
        final List<ChatEntity> allByUserId = repository.findAllByUserIdAndType(userId, type.toString());
        List<ChatResponse> responses = mapper.toResponses(allByUserId);
        return setUserIds(responses);
    }

    @Override
    public Page<ChatResponse> findAll(Pageable pageable) {
        final Page<ChatEntity> all = repository.findAll(pageable);
        Page<ChatResponse> map = all.map(mapper::toResponse);
        final List<ChatResponse> withUsers = setUserIds(map.stream().toList());
        return new PageImpl<>(withUsers);
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

    private List<ChatResponse> setUserIds(List<ChatResponse> chats) {
        final List<Long> chatIds = chats.stream()
                .map(ChatResponse::getId)
                .toList();
        List<UserIdDto> userIdDtos = jdbcRepository.findUserIds(chatIds);
        Map<Long, List<Long>> map = new HashMap<>();
        userIdDtos.forEach(dto -> map.put(dto.getChatId(), dto.getUserIds()));
        chats.forEach(chat -> {
            chat.setUserIds(map.get(chat.getId()));
        });
        return chats;
    }

}
