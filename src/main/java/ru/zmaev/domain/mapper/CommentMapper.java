package ru.zmaev.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.zmaev.domain.dto.response.CommentResponseDto;
import ru.zmaev.domain.entity.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper extends EntityMapper<Comment, CommentResponseDto, CommentResponseDto> {
    @Override
    @Mapping(target = "buildId", source = "comment.build.id")
    @Mapping(target = "userId", source = "comment.user.id")
    CommentResponseDto toDto(Comment comment);
}
