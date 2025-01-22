package com.ddc.chat.service.impl;

import com.ddc.chat.controller.request.ChangeUsersRequest;
import com.ddc.chat.controller.request.CreateChatRequest;
import com.ddc.chat.controller.request.UpdateChatRequest;
import com.ddc.chat.controller.response.ChatResponse;
import com.ddc.chat.entity.ChatEntity;
import com.ddc.chat.entity.Users;
import com.ddc.chat.enums.ChatType;
import com.ddc.chat.enums.ChangeUsersType;
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
        if (Objects.equals(createChatRequest.getType(), ChatType.PRIVATE) && createChatRequest.getUserIds()
                .size() != 2) {
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
        final List<ChatEntity> allByUserId = repository.findAllByUsers_Id(userId);
        return mapper.toResponses(allByUserId);
    }

    @Override
    public List<ChatResponse> findAllByUserIdAndType(Long userId, ChatType type) {
        final List<ChatEntity> allByUserId = repository.findAllByUsers_IdAndType(userId, type);
        return mapper.toResponses(allByUserId);
    }

    @Override
    public Page<ChatResponse> findAll(Pageable pageable) {
        final Page<ChatEntity> all = repository.findAll(pageable);
        return all.map(mapper::toResponse);
    }

    @Override
    public void update(UpdateChatRequest updateChatRequest, Long id) {
        final ChatEntity entity = mapper.toEntity(updateChatRequest);
        entity.setId(id);
        repository.save(entity);
    }

    @Override
    public void delete(Long chatId) {
        repository.deleteById(chatId);
    }

    @Override
    public boolean changeUsers(ChangeUsersRequest changeUsersRequest) {
        return changeUsersRequest.getType() == ChangeUsersType.ADD ? addUsers(changeUsersRequest) :
                changeUsersRequest.getType() == ChangeUsersType.DELETE && removeUsers(changeUsersRequest);
    }

    private boolean addUsers(ChangeUsersRequest changeUsersRequest) {
        final ChatEntity chat = repository.findById(changeUsersRequest.getChatId())
                .orElseThrow(() -> new RuntimeException("There is not chat " +
                                                                "with id" +
                                                                " " + changeUsersRequest.getChatId()));
        if((changeUsersRequest.getUserIds().size() > 1 || chat.getUsers().size() == 2) && chat.getType() == ChatType.PRIVATE) {
            throw new RuntimeException("In private chat cannot be more than 2 users");
        }
        final List<Users> users = changeUsersRequest.getUserIds()
                .stream()
                .map(Users::new)
                .toList();
        chat.getUsers().addAll(users);
        repository.save(chat);
        return !chat.getUsers().isEmpty();
    }

    private boolean removeUsers(ChangeUsersRequest changeUsersRequest) {
        repository.deleteUsersByChatIdAndUsers_IdIn(changeUsersRequest.getChatId(), changeUsersRequest.getUserIds());
        final ChatEntity chat = repository.findById(changeUsersRequest.getChatId())
                .orElseThrow(() -> new RuntimeException("There is not chat " +
                                                                "with id" +
                                                                " " + changeUsersRequest.getChatId()));
        return !chat.getUsers().isEmpty();
    }

}
