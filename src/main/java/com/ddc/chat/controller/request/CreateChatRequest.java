package com.ddc.chat.controller.request;

import com.ddc.chat.enums.ChatType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateChatRequest {

    private String name;

    private ChatType type;

    private List<Long> userIds;

}
