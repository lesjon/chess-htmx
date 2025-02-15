package nl.leonklute.chesshtmx.db.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "puzzles")
public class PuzzleEntity {
    @Id
    public String puzzleId;

    public String FEN;
    public String moves;
    public Integer rating;
    public Integer ratingDeviation;
    public Integer popularity;
    public Integer nbPlays;
    public String themes;
    public String gameUrl;
    public String openingTags;

}