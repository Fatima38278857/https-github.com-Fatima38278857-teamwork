package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.UserEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {

    CommentEntity toComment(CreateOrUpdateComment createdComment);
    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "authorImage", source = "author", qualifiedByName = "pathToImageEntity")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    CommentDTO toDto(CommentEntity commentEntity);

    CreateOrUpdateComment toCreatedComment(CommentEntity commentEntity);

    @Named("pathToImageEntity")
    default String pathToImageEntity(UserEntity author) {
        Image image = author.getImage();
        if (image == null) {
            return null;
        }
        return "/image/" + image.getId();
    }
}