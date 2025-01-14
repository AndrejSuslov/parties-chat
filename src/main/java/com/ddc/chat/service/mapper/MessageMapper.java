package com.ddc.chat.service.mapper;

import com.ddc.chat.controller.request.CreateMessageRequest;
import com.ddc.chat.controller.request.UpdateMessageRequest;
import com.ddc.chat.controller.response.MessageResponse;
import com.ddc.chat.entity.ChatEntity;
import com.ddc.chat.entity.ChatMessage;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.FIELD)
public interface MessageMapper {

    @Mapping(target = "chat", source = "chatId", qualifiedByName = "mapChatIdToChat")
    ChatMessage toEntity(CreateMessageRequest request);

    @Mapping(target = "chatId", source = "chat", qualifiedByName = "mapChatToChatId")
    MessageResponse toResponse(ChatMessage chatMessage);

    @Mapping(target = "chat", source = "chatId", qualifiedByName = "mapChatIdToChat")
    List<ChatMessage> toEntities(List<CreateMessageRequest> chatMessages);

    @Mapping(target = "chatId", source = "chat", qualifiedByName = "mapChatToChatId")
    List<MessageResponse> toResponses(List<ChatMessage> chatMessages);

    ChatMessage toEntity (UpdateMessageRequest request);

    @Named("mapChatToChatId")
    default Long mapChatToChatId(ChatEntity chatEntity) {
        return chatEntity.getId();
    }

    @Named("mapChatIdToChat")
    default ChatEntity mapChatIdToChat(Long chatId) {
        return new ChatEntity(chatId);
    }

}
