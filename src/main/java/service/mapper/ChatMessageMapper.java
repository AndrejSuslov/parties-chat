package service.mapper;

import controller.request.CreateChatMessageRequest;
import controller.request.UpdateMessageRequest;
import controller.response.ChatMessageResponse;
import entity.ChatMessage;
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
