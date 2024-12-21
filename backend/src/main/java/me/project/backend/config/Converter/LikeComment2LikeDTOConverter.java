package me.project.backend.config.Converter;

import me.project.backend.domain.LikeComment;
import me.project.backend.payload.dto.LikeDTO;
import org.modelmapper.AbstractConverter;

public class LikeComment2LikeDTOConverter extends AbstractConverter<LikeComment, LikeDTO> {
    @Override
    protected LikeDTO convert(LikeComment source) {
        return LikeDTO.builder()
                .username(source.getUsername())
                .reaction(source.getReaction())
                .type(LikeDTO.Type.COMMENT)
                .build();
    }
}
