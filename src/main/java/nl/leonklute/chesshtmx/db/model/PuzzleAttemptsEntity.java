package nl.leonklute.chesshtmx.db.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;

@Getter
@Entity(name = "puzzle_attempts")
public class PuzzleAttemptsEntity {
    @Id
    private Long id;

    @OneToOne
    private PuzzleEntity puzzleEntity;

}