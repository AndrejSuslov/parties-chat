package service.mapper;

import controller.request.CreateChatRequest;
import controller.request.UpdateChatRequest;
import controller.response.ChatResponse;
import entity.ChatEntity;
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
