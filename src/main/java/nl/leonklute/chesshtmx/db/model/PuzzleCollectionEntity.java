package nl.leonklute.chesshtmx.db.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.util.List;

@Entity(name = "puzzle_collections")
public class PuzzleCollectionEntity {
    @Id
    long id;

    @OneToOne
    UserEntity user;

    @OneToMany
    List<PuzzleAttemptsEntity> puzzleAttemptsEntities;

    public boolean containsPuzzleWithId(String puzzleId) {
        return puzzleAttemptsEntities.stream()
                .anyMatch(p -> puzzleId.equals(p.getPuzzleEntity().getPuzzleId()));
    }

}
