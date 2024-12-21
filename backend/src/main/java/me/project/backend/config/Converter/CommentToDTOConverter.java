package me.project.backend.config.Converter;

import me.project.backend.domain.Comment;
import me.project.backend.payload.dto.CommentDTO;
import me.project.backend.service.LikeService;
import org.modelmapper.AbstractConverter;
import org.springframework.beans.factory.annotation.Autowired;

public class CommentToDTOConverter extends AbstractConverter<Comment, CommentDTO> {

    @Autowired
    private LikeService likeService;

    @Override
    protected CommentDTO convert(Comment source) {
        return CommentDTO.builder()
                .id(source.getId())
                .content(source.getContent())
                .username(source.getUsername())
                .reaction(likeService.getUserReactionByCommentId(source.getId())).build();
    }
}
