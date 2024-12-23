package entity.metadata;

import jakarta.persistence.metamodel.StaticMetamodel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@StaticMetamodel(value = ChatMessage_.class)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final public class ChatEntity_ {

    public static final String TABLE = "chats";
    public static final String ID = "id";
    public static final String NAME = "name";
    public final static String CREATED_AT = "created_at";
    public final static String UPDATED_AT = "updated_at";
    public final static String DELETED_AT = "deleted_at";

}
