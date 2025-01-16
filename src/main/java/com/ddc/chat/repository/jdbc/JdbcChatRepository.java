package com.ddc.chat.repository.jdbc;

import com.ddc.chat.util.UserIdDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class JdbcChatRepository {

    private final static String SELECT_USER_IDS_QUERY = """
            SELECT ch.id as chat_id, array_agg(m2muc.user_id) as user_ids FROM m2m_users_chats m2muc
                LEFT JOIN chats ch ON ch.id = m2muc.chat_id
            WHERE m2muc.chat_id IN (:chat_id)
            AND ch.deleted_at IS NULL
            GROUP BY ch.id;
            """;

    private final static String INSERT_USER_IDS_AND_CHAT_ID_QUERY = """
            INSERT INTO m2m_users_chats (chat_id, user_id) VALUES (:chat_id, :user_id)
            """;

    private final static String CHAT_ID_QUERY_PARAM = "chat_id";

    private final static String USER_ID_QUERY_PARAM = "user_id";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<UserIdDto> findUserIds(List<Long> ids){
        try {
            return jdbcTemplate.query(SELECT_USER_IDS_QUERY,
                                      Collections.singletonMap(CHAT_ID_QUERY_PARAM, ids),
                                      ((rs, rowNum) -> new UserIdDto(rs.getLong(1),
                                                                                        parseString(
                                                                                                rs.getString(2)
                                                                                                        .replaceAll("[{ }]", "")))));
        } catch (NullPointerException e) {
            return new ArrayList<>();
        }
    }

    public void saveUsers(List<Long> chatIds, List<Long> userIds){
        try {
            Map<String, Long>[] params = new HashMap[userIds.size()];
            for (int i = 0; i < userIds.size(); i++) {
                Map<String, Long> param = new HashMap<>();
                param.put(USER_ID_QUERY_PARAM, userIds.get(i));
                param.put(CHAT_ID_QUERY_PARAM, chatIds.get(i));
                params[i] = param;
            }
            jdbcTemplate.batchUpdate(INSERT_USER_IDS_AND_CHAT_ID_QUERY, params);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private List<Long> parseString(String str) {
        return Arrays.stream(str.split(","))
                .map(Long::parseLong)
                .toList();
    }

}
