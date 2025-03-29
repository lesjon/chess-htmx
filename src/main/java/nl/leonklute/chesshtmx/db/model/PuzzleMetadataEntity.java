package nl.leonklute.chesshtmx.db.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity(name = "puzzle_metadatas")
public class PuzzleMetadataEntity {
    @Id
    private long id;

    private Boolean active;

    private String additionalMoves;

    private Set<Long> likes;
    private Set<Long> dislikes;

    @OneToOne
    @JoinColumn(name = "puzzle_id", nullable = false)
    private PuzzleEntity puzzle;

    public void removeDislikeIfExists(long userId) {
        if(dislikes == null){
            return;
        }
        if(!dislikes.contains(userId)){
           return;
        }
        dislikes = new HashSet<>(dislikes);
        dislikes.remove(userId);
    }

    public boolean addLike(long userId) {
        removeDislikeIfExists(userId);
        if (likes == null){
            this.likes = Set.of(userId);
            return true;
        }
        if (likes.contains(userId)) {
            return false;
        }
        Set<Long> newLikes = new HashSet<>(this.likes);
        newLikes.add(userId);
        this.likes = newLikes;
        return true;
    }

    @Override
    public String toString() {
        return "PuzzleMetadataEntity{" +
                "id=" + id +
                ", active=" + active +
                ", additionalMoves='" + additionalMoves + '\'' +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                ", puzzle=" + puzzle +
                '}';
    }

    public void removeLikeIfExists(long userId) {
        if(likes == null){
            return;
        }
        if(!likes.contains(userId)){
            return;
        }
        likes = new HashSet<>(likes);
        likes.remove(userId);
    }

    public boolean addDislike(long userId) {
        removeLikeIfExists(userId);
        if (dislikes == null){
            this.dislikes = Set.of(userId);
            return true;
        }
        if (dislikes.contains(userId)) {
            return false;
        }
        Set<Long> newDislikes = new HashSet<>(this.dislikes);
        newDislikes.add(userId);
        this.dislikes = newDislikes;
        return true;
    }
}
