package com.ddc.chat.service.mapper;

import com.ddc.chat.controller.request.CreateMessageRequest;
import com.ddc.chat.controller.request.UpdateMessageRequest;
import com.ddc.chat.controller.response.MessageResponse;
import com.ddc.chat.entity.ChatMessage;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.FIELD)
public interface ChatMessageMapper {

    ChatMessage toEntity(CreateMessageRequest request);

    MessageResponse toResponse(ChatMessage chatMessage);

    List<ChatMessage> toEntities(List<CreateMessageRequest> chatMessages);

    List<MessageResponse> toResponses(List<ChatMessage> chatMessages);

    ChatMessage toEntity (UpdateMessageRequest request);

}
