package com.ddc.chat.service.mapper;

import com.ddc.chat.controller.request.CreateChatRequest;
import com.ddc.chat.controller.request.UpdateChatRequest;
import com.ddc.chat.controller.response.ChatResponse;
import com.ddc.chat.entity.ChatEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.FIELD)
public interface ChatMapper {

    ChatEntity toEntity(CreateChatRequest createChatRequest);

    ChatResponse toResponse(ChatEntity chatEntity);

    ChatEntity toEntity(UpdateChatRequest updateChatRequest);

    List<ChatResponse> toResponses(List<ChatEntity> chatEntities);

}
