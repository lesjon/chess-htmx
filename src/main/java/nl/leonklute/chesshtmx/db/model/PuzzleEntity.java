package nl.leonklute.chesshtmx.db.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "puzzles")
public class PuzzleEntity {
    @Id
    private String puzzleId;

    private String FEN;
    private String moves;
    private Integer rating;
    private Integer ratingDeviation;
    private Integer popularity;
    private Integer nbPlays;
    private String themes;
    private String gameUrl;
    private String openingTags;

    @OneToOne(mappedBy = "puzzle", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private PuzzleMetadataEntity puzzleMetadataEntity;

}