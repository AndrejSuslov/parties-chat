package com.ddc.chat.repository;

import com.ddc.chat.util.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcChatRepository {

    private final static String QUERY = """
                SELECT ch.id, array_agg(m2muc.user_id) FROM m2m_users_chats m2muc
                    LEFT JOIN chats ch ON ch.id = m2muc.chat_id
                WHERE m2muc.chat_id IN (:chatId)
                AND ch.deleted_at IS NULL
                GROUP BY ch.id;
            """;

    private final static String QUERY_PARAM = "chat_id";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<UserDto> findUsers(List<Long> ids) {
        try {
            return jdbcTemplate.query(QUERY,
                                      Collections.singletonMap(QUERY_PARAM, ids),
                                      ((rs, rowNum) -> new UserDto(rs.getLong(1),
                                                                   Arrays.asList((Long[]) rs.getArray(2).getArray()))));
        } catch (NullPointerException e) {
            return new ArrayList<>();
        }
    }

}
