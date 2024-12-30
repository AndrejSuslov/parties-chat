package controller;

import controller.request.CreateChatMessageRequest;
import entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import service.ChatMessageService;

/*todo: "http://localhost:8080/ws/chat/sendMessage/{chatId}" - сюда с фронта отправляются сообщения и попадают
todo:  в проаннотированный метод sendMessage()
todo: "http://localhost:8080/ws/topic/{chatId}" - сюда после бэкенда отправляются сообщения и попадают к юзерам,
todo: кто подписан на канал "/topic/{chatId}" */

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat/sendMessage/{chatId}")
    @SendTo("/topic/{chatId}")
    public ChatMessage sendMessage(@Payload CreateChatMessageRequest chatMessage,
                                   @PathVariable Long chatId) {
        return chatMessageService.create(chatMessage);
    }

    @MessageMapping("/chat/addUser")
    @SendTo("/topic/{chatId}")
    public ChatMessage addUser(@Payload CreateChatMessageRequest chatMessage,
                               @PathVariable Long chatId,
                               SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessageService.create(chatMessage);
    }

    @MessageMapping("/chat/deleteUser")
    @SendTo("/topic/{chatId}")
    public ChatMessage deleteUser(@Payload CreateChatMessageRequest chatMessage,
                                  @PathVariable Long chatId,
                                  SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().remove(chatMessage.getSender());
        return chatMessageService.create(chatMessage);
    }

}
