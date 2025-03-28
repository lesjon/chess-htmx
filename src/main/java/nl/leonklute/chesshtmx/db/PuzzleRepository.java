package nl.leonklute.chesshtmx.db;

import nl.leonklute.chesshtmx.db.model.PuzzleEntity;
import nl.leonklute.chesshtmx.service.model.PuzzleListing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PuzzleRepository extends CrudRepository<PuzzleEntity, String>, PagingAndSortingRepository<PuzzleEntity, String> {

    Page<PuzzleListing> findAllBy(Pageable pageable);
    Page<PuzzleListing> findAllByThemesContaining(Pageable pageable, String theme);
    List<PuzzleListing> findFirst10ByRating(int rating, Sort sort);
}
