package com.ddc.chat.controller;

import com.ddc.chat.controller.request.CreateMessageRequest;
import com.ddc.chat.entity.ChatMessage;
import com.ddc.chat.enums.MessageType;
import com.ddc.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

/*
Действие                -------------------------------------   URL
_________________________________________________________________________________________________________________
Отправка сообщения      -------------------------------------   ws://localhost:8080/ws/app/chat/sendMessage/{chatId}
Подписка на чат	        -------------------------------------   ws://localhost:8080/ws/topic/{chatId}
Добавление пользователя	-------------------------------------   ws://localhost:8080/ws/app/chat/sendMessage/{chatId} __ с типом JOIN
Удаление пользователя   -------------------------------------   ws://localhost:8080/ws/app/chat/sendMessage/{chatId} __ c типом LEFT
todo: ко всему этому добавляются идентифиакторы от SockJS ??????????????? ПОКА БЕЗ НЕГО
todo: нужно подумать про идентификаторы от SockJS, т.к. они всегда разные и ломают смысл отправки сообщения в канал
*/

@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/sendMessage/{chatId}")
    public void sendMessage(@Payload CreateMessageRequest chatMessage,
                            @DestinationVariable Long chatId,
                            SimpMessageHeaderAccessor headerAccessor) {
        if (chatMessage.getType().equals(MessageType.JOIN)) {
            headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
            String notification = "Пользователь " + chatMessage.getSender() + " вошел в чат.";
            chatMessage.setContent(notification);
        } else if (chatMessage.getType().equals(MessageType.LEAVE)) {
            headerAccessor.getSessionAttributes().remove(chatMessage.getSender());
            String notification = "Пользователь " + chatMessage.getSender() + " вышел из чата.";
            chatMessage.setContent(notification);
        }
        ChatMessage savedMessage = messageService.create(chatMessage, chatId);
        messagingTemplate.convertAndSend("/topic/" + chatId, savedMessage);
    }

}

