package com.ddc.chat.service.impl;

import com.ddc.chat.controller.request.CreateChatRequest;
import com.ddc.chat.controller.request.UpdateChatRequest;
import com.ddc.chat.controller.response.ChatResponse;
import com.ddc.chat.entity.ChatEntity;
import com.ddc.chat.enums.ChatType;
import com.ddc.chat.repository.JdbcChatRepository;
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
        if(Objects.equals(createChatRequest.getType(), "PRIVATE") && createChatRequest.getUserIds().size() > 2) {
            throw new RuntimeException("In private chat cannot be more than 2 users");
        }
        final ChatEntity entity = mapper.toEntity(createChatRequest);
        return repository.save(entity).getId();
    }

    @Override
    public ChatResponse findById(Long id) {
        final ChatEntity chat = repository.findById(id).orElseThrow(RuntimeException::new);
        List<ChatEntity> chatEntity = setUserIds(Collections.singletonList(chat));
        return mapper.toResponse(chatEntity.get(0));
    }

    @Override
    public ChatResponse findByName(String name) {
        ChatEntity chat = repository.findByName(name);
        List<Long> userIdsByName = repository.findUserIdsByName(name);
        chat.setUserIds(userIdsByName);
        return mapper.toResponse(chat);
    }

    @Override
    public List<ChatResponse> findAllByUserId(Long userId) {
        final List<ChatEntity> allByUserId = repository.findAllByUserId(userId);
        final List<ChatEntity> withUsers = setUserIds(allByUserId);
        return mapper.toResponses(withUsers);
    }

    @Override
    public List<ChatResponse> findAllByUserIdAndType(Long userId, ChatType type) {
        final List<ChatEntity> allByUserId = repository.findAllByUserIdAndType(userId, type.toString());
        final List<ChatEntity> withUsers = setUserIds(allByUserId);
        return mapper.toResponses(withUsers);
    }

    @Override
    public Page<ChatResponse> findAll(Pageable pageable) {
        final Page<ChatEntity> all = repository.findAll(pageable);
        final List<ChatEntity> withUsers = setUserIds(all.stream().toList());
        return new PageImpl<>(withUsers).map(mapper::toResponse);
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

    private List<ChatEntity> setUserIds(List<ChatEntity> chats) {
        final List<Long> chatIds = chats.stream()
                .map(ChatEntity::getId)
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
