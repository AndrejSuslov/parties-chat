package service.impl;

import controller.request.CreateChatMessageRequest;
import controller.request.UpdateMessageRequest;
import controller.response.ChatMessageResponse;
import entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import repository.ChatMessageRepository;
import service.ChatMessageService;
import service.mapper.ChatMessageMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository repository;
    private final ChatMessageMapper mapper;

    @Override
    public ChatMessage create (CreateChatMessageRequest request) {
        final ChatMessage entity = mapper.toEntity(request);
        return repository.save(entity);
    }

    @Override
    public List<ChatMessageResponse> findAllByChatIdAndSender(Long chatId, String sender) {
        final List<ChatMessage> all = repository.findAllByChatIdAndSender(chatId, sender);
        return mapper.toResponses(all);
    }

    @Override
    public List<ChatMessageResponse> findAllByChatId(Long chatId) {
        List<ChatMessage> all = repository.findAllByChatId(chatId);
        return mapper.toResponses(all);
    }

    @Override
    public Long update(Long chatId, UpdateMessageRequest updateMessageRequest) {
        final ChatMessage entity = mapper.toEntity(updateMessageRequest);
        entity.getChatEntity().setId(chatId);
        return repository.save(entity).getId();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

}
