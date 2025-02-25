package nl.leonklute.chesshtmx.db.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "puzzle_metadatas")
public class PuzzleMetadataEntity {
    @Id
    private long id;

    private Boolean active;

    private String additionalMoves;

    @OneToOne
    @JoinColumn(name = "puzzle_id", nullable = false)
    private PuzzleEntity puzzle;
}
