package com.ddc.chat.service.mapper;

import com.ddc.chat.controller.request.CreateChatRequest;
import com.ddc.chat.controller.request.UpdateChatRequest;
import com.ddc.chat.controller.response.ChatResponse;
import com.ddc.chat.entity.ChatEntity;
import com.ddc.chat.entity.Users;
import org.apache.catalina.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.FIELD)
public interface ChatMapper {

    @Mapping(target = "users", source = "userIds", qualifiedByName = "mapUserIdsToUsers")
    ChatEntity toEntity(CreateChatRequest createChatRequest);

    @Mapping(target = "userIds", source = "users", qualifiedByName = "mapUsersToUserIds")
    ChatResponse toResponse(ChatEntity chatEntity);

    ChatEntity toEntity(UpdateChatRequest updateChatRequest);

    @Mapping(target = "userIds", source = "users", qualifiedByName = "mapUsersToUserIds")
    List<ChatResponse> toResponses(List<ChatEntity> chatEntities);

    @Named("mapUsersToUserIds")
    default List<Long> mapUsersToUserIds(List<Users> users) {
        return users.stream()
                .map(Users::getId)
                .toList();
    }

    @Named("mapUserIdsToUsers")
    default List<Users> mapUserIdsToUsers(List<Long> userIds) {
        return userIds.stream()
                .map(Users::new)
                .toList();
    }
}
