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

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    //todo: проедлать все такие операци, когда юзер находит что-то по чем-то (сообщения, приватные чаты, чаты оп
    // имени, чаты по id и так далее)
    private final ChatRepository repository;
    private final ChatMapper mapper;

    @Override
    public Long create(CreateChatRequest createChatRequest) {
        final ChatEntity entity = mapper.toEntity(createChatRequest);
        return repository.save(entity).getId();
    }

    @Override
    public ChatResponse findById(Long id) {
        final ChatEntity chat = repository.findById(id).orElseThrow(RuntimeException::new);
        final List<Long> allUserIds = repository.findAllUserIdsByIdOrName(id, "");
        chat.setUserIds(allUserIds);
        return mapper.toResponse(chat);
    }

    @Override
    public ChatResponse findByName(String name) {
        ChatEntity chat = repository.findByName(name);
        final List<Long> allUserIds = repository.findAllUserIdsByIdOrName(0L, name);
        chat.setUserIds(allUserIds);
        return mapper.toResponse(chat);
    }

    @Override
    public List<ChatResponse> findAllByUserId(Long userId) {
        final List<ChatEntity> allByUserId = repository.findAllByUserId(userId);
        allByUserId
                .forEach(chat -> {
                    final List<Long> allUserIdsByIdOrName = repository.findAllUserIdsByIdOrName(chat.getId(), "");
                    chat.setUserIds(allUserIdsByIdOrName);
                });
        return mapper.toResponses(allByUserId);
    }

    @Override
    public List<ChatResponse> findAllPrivateByUserId(Long userId) {
        final List<ChatEntity> allByUserId = repository.findAllByUserIdAndType(userId, ChatType.PRIVATE.toString());
        allByUserId
                .forEach(chat -> {
                    final List<Long> allUserIdsByIdOrName = repository.findAllUserIdsByIdOrName(chat.getId(), "");
                    chat.setUserIds(allUserIdsByIdOrName);
                });
        return mapper.toResponses(allByUserId);
    }

    @Override
    public List<ChatResponse> findAllPublicByUserId(Long userId) {
        final List<ChatEntity> allByUserId = repository.findAllByUserIdAndType(userId, ChatType.PUBLIC.toString());
        allByUserId
                .forEach(chat -> {
                    final List<Long> allUserIdsByIdOrName = repository.findAllUserIdsByIdOrName(chat.getId(), "");
                    chat.setUserIds(allUserIdsByIdOrName);
                });
        return mapper.toResponses(allByUserId);
    }

    @Override
    public Page<ChatResponse> findAll(Pageable pageable) {
        final Page<ChatEntity> all = repository.findAll(pageable);
        all
                .forEach(chat -> {
                    final List<Long> allUserIdsByIdOrName = repository.findAllUserIdsByIdOrName(chat.getId(), "");
                    chat.setUserIds(allUserIdsByIdOrName);
                });
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
