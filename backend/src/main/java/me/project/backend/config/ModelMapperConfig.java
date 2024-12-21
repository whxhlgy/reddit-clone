package me.project.backend.config;

import me.project.backend.config.Converter.CommentToDTOConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper(
            CommentToDTOConverter commentToDTOConverter
    ) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        modelMapper.addConverter(commentToDTOConverter);
        return modelMapper;
    }

    @Bean
    public CommentToDTOConverter commentDTOConverter() {
        return new CommentToDTOConverter();
    }

}
