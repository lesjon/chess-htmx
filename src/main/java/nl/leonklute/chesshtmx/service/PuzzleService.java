package nl.leonklute.chesshtmx.service;

import nl.leonklute.chesshtmx.db.PuzzleRepository;
import nl.leonklute.chesshtmx.db.model.PuzzleEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PuzzleService {

    private final PuzzleRepository puzzleRepository;

    public PuzzleService(PuzzleRepository puzzleRepository) {
        this.puzzleRepository = puzzleRepository;
    }

    public Optional<PuzzleEntity> getPuzzle(String puzzleId) {
        return puzzleRepository.findById(puzzleId);
    }

    public List<PuzzleEntity> getPaginatedPuzzles(int page) {
        return puzzleRepository.findAll(PageRequest.of(page, 20)).getContent();

    }
}
