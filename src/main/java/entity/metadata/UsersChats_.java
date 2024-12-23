package entity.metadata;

import jakarta.persistence.metamodel.StaticMetamodel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@StaticMetamodel(value = ChatMessage_.class)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final public class UsersChats_ {

    public static final String TABLE = "m2m_users_chats";
    public static final String USER_ID = "user_id";
    public static final String CHAT_ID = "chat_id";

}
