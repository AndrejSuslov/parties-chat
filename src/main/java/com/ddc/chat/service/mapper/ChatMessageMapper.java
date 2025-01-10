package com.ddc.chat.service.mapper;

import com.ddc.chat.controller.request.CreateMessageRequest;
import com.ddc.chat.controller.request.UpdateMessageRequest;
import com.ddc.chat.controller.response.MessageResponse;
import com.ddc.chat.entity.ChatEntity;
import com.ddc.chat.entity.ChatMessage;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.FIELD)
public interface ChatMessageMapper {

    @Mapping(target = "chat", source = "chatId", qualifiedByName = "mapChatIdToChat")
    ChatMessage toEntity(CreateMessageRequest request);

    MessageResponse toResponse(ChatMessage chatMessage);

    List<ChatMessage> toEntities(List<CreateMessageRequest> chatMessages);

    List<MessageResponse> toResponses(List<ChatMessage> chatMessages);

    ChatMessage toEntity (UpdateMessageRequest request);

    @Named("mapChatIdToChat")
    default ChatEntity mapChatIdToChat (String value) {
        ChatEntity chat = new ChatEntity();
        chat.setId(Long.parseLong(value));
        return chat;
    }

}
