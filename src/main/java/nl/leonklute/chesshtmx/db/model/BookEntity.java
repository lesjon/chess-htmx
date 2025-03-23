package nl.leonklute.chesshtmx.db.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity(name = "books")
public class BookEntity {

    @Id
    long id;

    String name;

    @OneToMany
    List<PuzzleMetadataEntity> puzzleMetadataList;

}
