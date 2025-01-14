package com.ddc.chat.repository;

import com.ddc.chat.entity.ChatEntity;
import com.ddc.chat.util.UserIdDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {

    @Query(value = """
            SELECT ch.id as id,
            ch.name as name,
            ch.type as type,
            ch.created_at as created_at,
            ch.updated_at as updated_at,
            ch.deleted_at as deleted_at
            FROM chats ch
                LEFT JOIN m2m_users_chats m2muc ON :userId = m2muc.user_id AND m2muc.chat_id = ch.id
            WHERE ch.deleted_at IS NULL
            ORDER BY ch.created_at DESC
           """, nativeQuery = true)
    List<ChatEntity> findAllByUserId(Long userId);

    Page<ChatEntity> findAll(Pageable pageable);

    @Query(value = """
            SELECT ch.id as id,
            ch.name as name,
            ch.type as type,
            ch.created_at as created_at,
            ch.updated_at as updated_at,
            ch.deleted_at as deleted_at FROM chats ch
                LEFT JOIN m2m_users_chats m2muc ON :userId = m2muc.user_id AND ch.id = m2muc.chat_id
            WHERE ch.deleted_at IS NULL
            AND ch.type = :type
            ORDER BY ch.created_at DESC
            """, nativeQuery = true)
    List<ChatEntity> findAllByUserIdAndType(Long userId, String type);

    ChatEntity findByName(String name);

    @Query(value = """
            SELECT ch.id as chat_id, array_agg(m2muc.user_id) as user_ids FROM m2m_users_chats m2muc
                LEFT JOIN chats ch ON ch.id = m2muc.chat_id
            WHERE m2muc.chat_id IN (:chatId)
            AND ch.deleted_at IS NULL
            GROUP BY ch.id;
            """, nativeQuery = true)
    List<UserIdDto> findAllUserIdsById(List<Long> chatId);

}
