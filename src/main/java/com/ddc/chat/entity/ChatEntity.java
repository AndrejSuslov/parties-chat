package com.ddc.chat.entity;

import com.ddc.chat.entity.metadata.ChatEntity_;
import com.ddc.chat.enums.ChatType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = ChatEntity_.TABLE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@SQLRestriction(value = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE " + ChatEntity_.TABLE + " SET deleted_at = now() " +
" WHERE id = ? AND deleted_at IS NULL")
public class ChatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ChatEntity_.ID)
    private Long id;

    @Column(name = ChatEntity_.NAME)
    private String name;

//    @ElementCollection
//    private List<Long> userIds = new ArrayList<>();

    @Column(name = ChatEntity_.TYPE)
    @Enumerated(value = EnumType.STRING)
    private ChatType type;

    @CreatedDate
    @Column(name = ChatEntity_.CREATED_AT, updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = ChatEntity_.UPDATED_AT, insertable = false)
    private LocalDateTime updatedAt;

    @Column(name = ChatEntity_.DELETED_AT, insertable = false)
    private LocalDateTime deletedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "m2m_users_chats",
            joinColumns = {@JoinColumn(name = "chat_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    private List<Users> users = new ArrayList<>();

    public ChatEntity(Long id){
        this.id = id;
    }

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
        ChatEntity that = (ChatEntity) o;
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
                "name = " + getName() + ", " +
                "createdAt = " + getCreatedAt() + ", " +
                "updatedAt = " + getUpdatedAt() + ", " +
                //"userIds = " + getUserIds() + ", " +
                "deletedAt = " + getDeletedAt() + ")";
    }

}
