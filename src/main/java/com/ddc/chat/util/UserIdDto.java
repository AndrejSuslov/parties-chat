package com.ddc.chat.util;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserIdDto {

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "user_ids")
    private List<Long> userIds;

}
