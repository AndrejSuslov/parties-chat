package com.ddc.chat.service.impl;

import com.ddc.chat.controller.request.CreateMessageRequest;
import com.ddc.chat.controller.request.UpdateMessageRequest;
import com.ddc.chat.controller.response.MessageResponse;
import com.ddc.chat.entity.ChatEntity;
import com.ddc.chat.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ddc.chat.repository.ChatMessageRepository;
import com.ddc.chat.service.ChatMessageService;
import com.ddc.chat.service.mapper.ChatMessageMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository repository;
    private final ChatMessageMapper mapper;

    @Override
    public ChatMessage create (CreateMessageRequest request, Long chatId) {
        final ChatMessage entity = mapper.toEntity(request);
        entity.setChat(new ChatEntity(chatId));
        return repository.save(entity);
    }

    @Override
    public List<MessageResponse> findAllByChatIdAndSender(Long chatId, String sender) {
        final List<ChatMessage> all = repository.findAllByIdAndSender(chatId, sender);
        return mapper.toResponses(all);
    }

    @Override
    public List<MessageResponse> findAllByChatId(Long chatId) {
        List<ChatMessage> all = repository.findAllByChatId(chatId);
        return mapper.toResponses(all);
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
