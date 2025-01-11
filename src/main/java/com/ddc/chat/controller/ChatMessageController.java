package com.ddc.chat.controller;

import com.ddc.chat.controller.request.CreateMessageRequest;
import com.ddc.chat.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import com.ddc.chat.service.ChatMessageService;
import org.springframework.web.bind.annotation.RestController;

/*
Действие                -------------------------------------   URL
_________________________________________________________________________________________________________________
Отправка сообщения      -------------------------------------   ws://localhost:8080/ws/app/chat/sendMessage/{chatId}
Подписка на чат	        -------------------------------------   ws://localhost:8080/ws/topic/{chatId}
Добавление пользователя	-------------------------------------   ws://localhost:8080/ws/app/chat/{chatId}/addUser
Удаление пользователя   -------------------------------------   ws://localhost:8080/ws/app/chat/{chatId}/deleteUser
todo: ко всему этому добавляются идентифиакторы от SockJS ???????????????
todo: нужно подумать про идентификаторы от SockJS, т.к. они всегда разные и ломают смысл отправки сообщения в канал
*/

@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/sendMessage/{chatId}")
    public void sendMessage(@Payload CreateMessageRequest chatMessage,
                            @DestinationVariable Long chatId) {
        ChatMessage savedMessage = chatMessageService.create(chatMessage, chatId);
        messagingTemplate.convertAndSend("/topic/" + chatId, savedMessage);
    }

    @MessageMapping("/chat/{chatId}/addUser")
    public void addUser(@Payload CreateMessageRequest request,
                        @DestinationVariable Long chatId,
                        SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", request.getSender());
        String notification = "Пользователь " + request.getSender() + " добавлен в чат.";
        messagingTemplate.convertAndSend("/topic/" + chatId, notification);
        chatMessageService.create(request, chatId);
    }

    @MessageMapping("/chat/{chatId}/removeUser")
    public void removeUser(@Payload CreateMessageRequest request,
                           @DestinationVariable Long chatId,
                           SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().remove(request.getSender());
        String notification = "Пользователь " + request.getSender() + " удалён из чата.";
        messagingTemplate.convertAndSend("/topic/" + chatId, notification);
        chatMessageService.create(request, chatId);
    }

}

