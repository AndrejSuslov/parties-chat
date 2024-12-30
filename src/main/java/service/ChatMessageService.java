package service;

import controller.request.CreateChatMessageRequest;
import controller.request.UpdateMessageRequest;
import controller.response.ChatMessageResponse;
import entity.ChatMessage;

import java.util.List;

public interface ChatMessageService {

    ChatMessage create(CreateChatMessageRequest createChatMessageRequest);

    List<ChatMessageResponse> findAllByChatId(Long chatId);

    List<ChatMessageResponse> findAllByChatIdAndSender(Long chatId, String sender);

    Long update(Long id, UpdateMessageRequest updateMessageRequest);

    void delete(Long id);

}
