package service;

import controller.request.UpdateChatRequest;
import controller.response.ChatResponse;
import controller.request.CreateChatRequest;
import entity.ChatEntity;

import java.util.List;

public interface ChatService {

    Long create(CreateChatRequest createChatRequest);

    ChatEntity findById(Long id);

    List<ChatResponse> findAllByUserId(Long userId);

    List<ChatResponse> findAll();

    Long update(UpdateChatRequest updateChatRequest);

    void delete(Long chatId);
}
