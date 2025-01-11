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
Добавление пользователя	-------------------------------------   ws://localhost:8080/ws/app/chat/addUser/{chatId}
Удаление пользователя   -------------------------------------   ws://localhost:8080/ws/app/chat/deleteUser/{chatId}
todo: ко всему этому добавляются идентифиакторы от SockJS
*/

@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    // Отправка сообщения в чат
    @MessageMapping("/chat/sendMessage/{chatId}")
    public void sendMessage(@Payload CreateMessageRequest chatMessage,
                            @DestinationVariable Long chatId) {
        ChatMessage savedMessage = chatMessageService.create(chatMessage);

        // Рассылаем сообщение всем подписчикам данного чата
        messagingTemplate.convertAndSend("/topic/" + chatId, savedMessage);
    }

    // Добавление пользователя в чат
    @MessageMapping("/chat/addUser/{chatId}")
    public void addUser(@Payload CreateMessageRequest request,
                        @DestinationVariable Long chatId,
                        SimpMessageHeaderAccessor headerAccessor) {
        // Сохраняем пользователя в сессии
        headerAccessor.getSessionAttributes().put("username", request.getSender());

        // Уведомляем чат о новом участнике
        String notification = "Пользователь " + request.getSender() + " добавлен в чат.";
        messagingTemplate.convertAndSend("/topic/" + chatId, notification);

        // Дополнительная логика (например, добавление в базу)
        chatMessageService.create(request);
    }

    // Удаление пользователя из чата
    @MessageMapping("/chat/removeUser/{chatId}")
    public void removeUser(@Payload CreateMessageRequest request,
                           @DestinationVariable Long chatId,
                           SimpMessageHeaderAccessor headerAccessor) {
        // Удаляем пользователя из сессии
        headerAccessor.getSessionAttributes().remove(request.getSender());

        // Уведомляем чат об удалении участника
        String notification = "Пользователь " + request.getSender() + " удалён из чата.";
        messagingTemplate.convertAndSend("/topic/" + chatId, notification);

        // Дополнительная логика (например, удаление из базы)
        chatMessageService.create(request);
    }

}

