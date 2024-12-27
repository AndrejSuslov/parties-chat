package repository;

import entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {

    @Query("""
            SELECT ch FROM ChatEntity ch
                LEFT JOIN UserEntity u ON u.id IN ch.userIds
            WHERE ch.deletedAt IS NULL 
            ORDER BY ch.createdAt DESC
            """)
    List<ChatEntity> findAllByUserId(Long userId);
}
