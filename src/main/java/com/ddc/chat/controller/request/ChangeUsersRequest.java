package com.ddc.chat.controller.request;

import com.ddc.chat.enums.ChangeUsersType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUsersRequest {

    private ChangeUsersType type;

    private Long chatId;

    private List<Long> userIds;

}
