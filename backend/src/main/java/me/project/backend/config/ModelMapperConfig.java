package me.project.backend.config;

import me.project.backend.config.Converter.CommentLike2LikeDTOConverter;
import me.project.backend.config.Converter.PostLike2LikeDTOConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper(
    ) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        modelMapper.addConverter(new CommentLike2LikeDTOConverter());
        modelMapper.addConverter(new PostLike2LikeDTOConverter());
        return modelMapper;
    }
}
