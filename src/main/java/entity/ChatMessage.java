package entity;

import entity.metadata.ChatMessage_;
import enums.MessageType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = ChatMessage_.TABLE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
@SQLRestriction(value = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE " + ChatMessage_.TABLE + " SET deleted_at = now() " +
        " WHERE id = ? AND deleted_at IS NULL")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ChatMessage_.ID)
    private Long id;

    @Column(name = ChatMessage_.SENDER)
    private String sender;

    @Column(name = ChatMessage_.CONTENT)
    private String content;

    @Column(name = ChatMessage_.TYPE)
    @Enumerated(EnumType.STRING)
    private MessageType type;

    @JoinColumn(name = ChatMessage_.CHAT_ID)
    @ManyToOne(targetEntity = ChatEntity.class, fetch = FetchType.LAZY)
    private ChatEntity chatEntity;

    @CreatedDate
    @Column(name = ChatMessage_.CREATED_AT, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = ChatMessage_.UPDATED_AT, insertable = false)
    private LocalDateTime updatedAt;

    @Column(name = ChatMessage_.DELETED_AT)
    private LocalDateTime deletedAt;

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        Class<?> oEffectiveClass =
                o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
                        : o.getClass();
        Class<?> thisEffectiveClass =
                this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
                        .getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) {
            return false;
        }
        ChatMessage that = (ChatMessage) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
                .getPersistentClass().hashCode() : getClass().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + getId() + ", " +
                "sender = " + getSender() + ", " +
                "content = " + getContent() + ", " +
                "chatEntity = " + getChatEntity() + ", " +
                "type = " + getType() + ", " +
                "createdAt = " + getCreatedAt() + ", " +
                "updatedAt = " + getUpdatedAt() + ", " +
                "deletedAt = " + getDeletedAt() + ")";
    }

}
