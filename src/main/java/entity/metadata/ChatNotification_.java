package entity.metadata;

import jakarta.persistence.metamodel.StaticMetamodel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@StaticMetamodel(value = ChatMessage_.class)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final public class ChatNotification_ {

    public static final String TABLE = "chat_notifications";
    public static final String ID = "id";
    public static final String SENDER_ID = "sender_id";
    public static final String SENDER_NAME = "sender_name";

}
