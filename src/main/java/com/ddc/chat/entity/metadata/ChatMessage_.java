package com.ddc.chat.entity.metadata;

import jakarta.persistence.metamodel.StaticMetamodel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@StaticMetamodel(value = ChatMessage_.class)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final public class ChatMessage_ {

    public final static String TABLE = "chat_messages";
    public final static String ID = "id";
    public final static String CHAT_ID = "chat_id";
    public final static String SENDER = "sender";
    public final static String CONTENT = "content";
    public final static String TYPE = "type";
    public final static String CREATED_AT = "created_at";
    public final static String UPDATED_AT = "updated_at";
    public final static String DELETED_AT = "deleted_at";

}
