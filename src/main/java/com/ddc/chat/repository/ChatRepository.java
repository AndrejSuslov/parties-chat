package com.ddc.chat.repository;

import com.ddc.chat.entity.ChatEntity;
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
            SELECT m2muc.user_id FROM m2m_users_chats m2muc
                LEFT JOIN chats ch ON ch.name = :name
            WHERE ch.deleted_at IS NULL
            AND ch.id = m2muc.chat_id
            """, nativeQuery = true)
    List<Long> findUserIdsByName(String name);

}
