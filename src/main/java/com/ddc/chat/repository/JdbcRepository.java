package com.ddc.chat.repository;

import com.ddc.chat.Model.UserDto;
import com.ddc.chat.repository.parser.ListParser;
import com.ddc.chat.util.UserIdDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcRepository {

    private final static String QUERY = """
            SELECT ch.id as chat_id, array_agg(m2muc.user_id) as user_ids FROM m2m_users_chats m2muc
                LEFT JOIN chats ch ON ch.id = m2muc.chat_id
            WHERE m2muc.chat_id IN (:chat_id)
            AND ch.deleted_at IS NULL
            GROUP BY ch.id;
            """;

    private final static String QUERY_PARAM = "chat_id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ListParser listParser;

    public List<UserIdDto> findUserIds(List<Long> ids){
        try {
            return jdbcTemplate.query(QUERY,
                                      Collections.singletonMap(QUERY_PARAM, ids),
                                      ((rs, rowNum) -> new UserIdDto(rs.getLong(1),
                                                                                        listParser.parseString(
                                                                                                rs.getString(2)
                                                                                                        .replaceAll("[{ }]", "")))));
        } catch (NullPointerException e) {
            return new ArrayList<>();
        }
    }
}
