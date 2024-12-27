package service.impl;

import controller.request.CreateChatRequest;
import controller.request.UpdateChatRequest;
import controller.response.ChatResponse;
import entity.ChatEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import repository.ChatRepository;
import service.ChatService;
import service.mapper.ChatMapper;

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
