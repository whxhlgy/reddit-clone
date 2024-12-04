package me.project.backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "community")
@Table(indexes = @Index(columnList = "name", unique = true))
@Data
@NoArgsConstructor
public class Community {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 20)
    @NotBlank
    private String name;

    @NotNull
    private String description;

    public Community(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
