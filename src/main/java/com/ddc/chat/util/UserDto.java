package com.ddc.chat.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long chatId;

    private List<Long> userIds;

    private Map<Long, List<Long>> map = Map.of(chatId, userIds);

    public List<Long> getUsers(Long key) {
        return map.get(key);
    }

    public UserDto(Long chatId, List<Long> userIds) {
        this.chatId = chatId;
        this.userIds = userIds;
    }

}
