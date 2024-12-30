package com.ddc.chat.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponse {

    private Long id;

    private String sender;

    private String content;

    private Long chatId;

    private LocalDateTime createdAt;

}
