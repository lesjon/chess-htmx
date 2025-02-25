package nl.leonklute.chesshtmx.db;

import nl.leonklute.chesshtmx.db.model.PuzzleMetadataEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PuzzleMetadataRepository extends CrudRepository<PuzzleMetadataEntity, Long> {
}
