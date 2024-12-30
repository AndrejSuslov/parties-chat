package com.ddc.chat.service.mapper;

import com.ddc.chat.controller.request.CreateChatMessageRequest;
import com.ddc.chat.controller.request.UpdateMessageRequest;
import com.ddc.chat.controller.response.ChatMessageResponse;
import com.ddc.chat.entity.ChatMessage;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.FIELD)
public interface ChatMessageMapper {

    ChatMessage toEntity(CreateChatMessageRequest request);

    ChatMessageResponse toResponse(ChatMessage chatMessage);

    List<ChatMessage> toEntities(List<CreateChatMessageRequest> chatMessages);

    List<ChatMessageResponse> toResponses(List<ChatMessage> chatMessages);

    ChatMessage toEntity (UpdateMessageRequest request);

}
