package entity;

import entity.metadata.ChatNotification_;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(name = ChatNotification_.TABLE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ChatNotification_.ID)
    private Long id;

    @Column(name = ChatNotification_.SENDER_ID)
    private Long senderId;

    @Column(name = ChatNotification_.SENDER_NAME)
    @Formula("""
            SELECT u.name FROM users u
            WHERE u.id = :senderId
            """)
    private String senderName;

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
        ChatNotification that = (ChatNotification) o;
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
                "senderId = " + getSenderId() + ", " +
                "senderName = " + getSenderName() + ")";
    }

}
