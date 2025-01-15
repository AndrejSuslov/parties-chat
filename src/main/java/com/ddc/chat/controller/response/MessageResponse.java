package com.ddc.chat.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {

    private Long id;

    private String sender;

    private String content;

    private Long chatId;

    private LocalDate date;

    private LocalTime time;

}
