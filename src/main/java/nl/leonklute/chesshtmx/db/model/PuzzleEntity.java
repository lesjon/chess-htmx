package nl.leonklute.chesshtmx.db.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "puzzles")
public class PuzzleEntity {
    @Id
    public String puzzleId;

    public String FEN;
    public Integer Rating;
    public Integer RatingDeviation;
    public Integer Popularity;
    public Integer NbPlays;
    public String themes;
    public String gameUrl;
    public String openingTags;

}