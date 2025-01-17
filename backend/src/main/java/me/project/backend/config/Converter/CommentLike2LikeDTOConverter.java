package me.project.backend.config.Converter;

import me.project.backend.domain.CommentLike;
import me.project.backend.payload.dto.LikeDTO;
import org.modelmapper.AbstractConverter;

public class CommentLike2LikeDTOConverter extends AbstractConverter<CommentLike, LikeDTO> {
    @Override
    protected LikeDTO convert(CommentLike source) {
        return LikeDTO.builder()
                .username(source.getUsername())
                .reaction(source.getReaction())
                .type(LikeDTO.Type.COMMENT)
                .build();
    }
}
