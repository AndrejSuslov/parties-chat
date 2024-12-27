package controller;

import entity.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import service.ChatService;

@Controller
public class ChatController {

    private SimpMessagingTemplate template;
    private ChatService chatService;

//    @MessageMapping({"/message"})
//    @SendTo({"/chatroom/public"})
//    public ChatMessage receiveMessage(ChatMessage message) {
//        this.chatService.save(message);
//        return message;
//    }
//
//    @MessageMapping({"/private-message"})
//    public void privateMessage(ChatMessage message) {
//        Long receiver = message.getSenderId();
//        this.template.convertAndSendToUser(receiver.toString(), "/private", message);
//        this.chatService.save(message);
//    }

    @MessageMapping("/chat/sendMessage/{chatId}")
    @SendTo("/topic/{chatId}")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage,
                                   @PathVariable Long chatId) {
        chatService.save(chatMessage);
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/{chatId}")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               @PathVariable Long chatId,
                               SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

}
