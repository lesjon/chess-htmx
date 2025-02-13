package nl.leonklute.chesshtmx.db;

import nl.leonklute.chesshtmx.db.model.PuzzleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PuzzleRepository extends CrudRepository<PuzzleEntity, String>, PagingAndSortingRepository<PuzzleEntity, String> {
}
