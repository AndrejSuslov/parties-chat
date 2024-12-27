package service;

import controller.request.UpdateChatRequest;
import controller.response.ChatResponse;
import controller.request.CreateChatRequest;

import java.util.List;

public interface ChatService {

    Long create(CreateChatRequest createChatRequest);

    List<ChatResponse> findAllByUserId(Long userId);

    List<ChatResponse> findAll();

    Long update(UpdateChatRequest updateChatRequest);

    void delete(Long chatId);
}
