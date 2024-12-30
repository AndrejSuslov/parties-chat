package com.ddc.chat.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateChatMessageRequest {

    private String sender;

    private String content;

    private Long chatId;

    private String messageType;

}
