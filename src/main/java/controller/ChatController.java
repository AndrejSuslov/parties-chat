package controller;

import entity.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import service.ChatService;

@Controller
public class ChatController {

    private SimpMessagingTemplate template;
    private ChatService chatService;

    @MessageMapping({"/message"})
    @SendTo({"/chatroom/public"})
    public ChatMessage receiveMessage(ChatMessage message) {
        this.chatService.save(message);
        return message;
    }

    @MessageMapping({"/private-message"})
    public void privateMessage(ChatMessage message) {
        Long receiver = message.getSenderId();
        this.template.convertAndSendToUser(receiver.toString(), "/private", message);
        this.chatService.save(message);
    }
}
