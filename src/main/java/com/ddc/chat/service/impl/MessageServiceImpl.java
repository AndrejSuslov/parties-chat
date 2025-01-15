package com.ddc.chat.service.impl;

import com.ddc.chat.controller.request.CreateMessageRequest;
import com.ddc.chat.controller.request.UpdateMessageRequest;
import com.ddc.chat.controller.response.MessageResponse;
import com.ddc.chat.entity.ChatEntity;
import com.ddc.chat.entity.ChatMessage;
import com.ddc.chat.repository.MessageRepository;
import com.ddc.chat.service.mapper.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.ddc.chat.service.MessageService;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository repository;
    private final MessageMapper mapper;

    @Override
    public ChatMessage create (CreateMessageRequest request, Long chatId) {
        final ChatMessage entity = mapper.toEntity(request);
        entity.setChat(new ChatEntity(chatId));
        return repository.save(entity);
    }

    @Override
    public Page<MessageResponse> findAllByChatIdAndSender(Long chatId, String sender, Pageable pageable) {
        final Page<ChatMessage> all = repository.findAllByChatIdAndSender(chatId, sender, pageable);
        return all.map(mapper::toResponse);
    }

    @Override
    public Page<MessageResponse> findAllByChatIdAndDate(Long chatId, String date, Pageable pageable) {
        final LocalDate dateTime = LocalDate.parse(date);
        final Page<ChatMessage> all = repository.findAllByChatIdAndDate(chatId, dateTime, pageable);
        return all.map(mapper::toResponse);
    }

    @Override
    public Page<MessageResponse> findAllByChatId(Long chatId, Pageable pageable) {
        Page<ChatMessage> all = repository.findAllByChatId(chatId, pageable);
        return all.map(mapper::toResponse);
    }

    @Override
    public Long update(Long chatId, UpdateMessageRequest updateMessageRequest) {
        final ChatMessage entity = mapper.toEntity(updateMessageRequest);
        entity.getChat().setId(chatId);
        return repository.save(entity).getId();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

}
