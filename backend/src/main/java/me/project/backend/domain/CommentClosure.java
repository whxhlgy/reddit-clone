package me.project.backend.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.GeneratorStrategy;

@Data
@Entity
@NoArgsConstructor
public class CommentClosure {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ancestor_id")
    private Comment ancestor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "descendant_id")
    private Comment descendant;

    private Integer distance;

    public CommentClosure(Comment ancestor, Comment descendant, Integer distance) {
        this.ancestor = ancestor;
        this.descendant = descendant;
        this.distance = distance;
    }
}
