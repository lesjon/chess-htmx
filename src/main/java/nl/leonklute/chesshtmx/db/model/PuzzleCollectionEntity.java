package nl.leonklute.chesshtmx.db.model;

import jakarta.persistence.*;

import java.util.List;

@Entity(name = "puzzle_collections")
public class PuzzleCollectionEntity {
    @Id
    long id;

    @OneToOne
    UserEntity userEntity;

    @OneToMany
    List<PuzzleAttemptsEntity> puzzleAttemptsEntities;

}
