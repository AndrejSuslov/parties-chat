package com.ddc.chat.repository;

import com.ddc.chat.entity.ChatEntity;
import com.ddc.chat.enums.ChatType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {

    List<ChatEntity> findAllByUsers_Id(Long userId);

    Page<ChatEntity> findAll(Pageable pageable);

    List<ChatEntity> findAllByUsers_IdAndType(Long userId, ChatType type);

    ChatEntity findByName(String name);

    @Query(value = """
        DELETE FROM m2m_users_chats m2muc
        WHERE m2muc.chat_id = :chatId
        AND m2muc.user_id IN (:userIds)
    """, nativeQuery = true)
    @Modifying
    @Transactional
    void deleteUsersByChatIdAndUsers_IdIn(Long chatId, List<Long> userIds);

}
