package me.project.backend.domain;

import java.util.Set;

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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 20, unique = true, nullable = false)
    @NotBlank
    private String name;

    @NotNull
    private String description;

    private long followerCount;

    @OneToMany(mappedBy = "community", fetch = FetchType.LAZY)
    private Set<Post> posts;

    public Community(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
