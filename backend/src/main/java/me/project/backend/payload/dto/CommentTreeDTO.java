package me.project.backend.payload.dto;

import lombok.Data;

import java.util.List;

@Data
public class CommentTreeDTO {

    private Long id;

    private String content;

    private List<CommentTreeDTO> children;

}
