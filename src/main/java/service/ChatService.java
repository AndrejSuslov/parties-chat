package service;

import entity.ChatMessage;
import org.springframework.stereotype.Service;
import repository.ChatMessageRepository;

@Service
public class ChatService {

    private ChatMessageRepository messageRepository;

    public void save(ChatMessage chatMessage) {
        messageRepository.save(chatMessage);
    }

}
