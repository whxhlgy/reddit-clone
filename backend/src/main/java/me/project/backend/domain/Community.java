package me.project.backend.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "community")
@Data
@NoArgsConstructor
public class Community {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 20)
    private String name;

    private String description;

    public Community(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
