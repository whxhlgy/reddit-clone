package me.project.backend.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Community implements Serializable {

    private Long id;

    private String description;
}
