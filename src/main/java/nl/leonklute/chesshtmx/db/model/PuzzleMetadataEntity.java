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

    private Set<String> likes;
    private Set<String> dislikes;

    @OneToOne
    @JoinColumn(name = "puzzle_id", nullable = false)
    private PuzzleEntity puzzle;

    public void removeDislikeIfExists(String username) {
        if(dislikes == null){
            return;
        }
        if(!dislikes.contains(username)){
           return;
        }
        dislikes = new HashSet<>(dislikes);
        dislikes.remove(username);
    }

    public boolean addLike(String username) {
        removeDislikeIfExists(username);
        if (likes == null){
            this.likes = Set.of(username);
            return true;
        }
        if (likes.contains(username)) {
            return false;
        }
        Set<String> newLikes = new HashSet<>(this.likes);
        newLikes.add(username);
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

    public void removeLikeIfExists(String username) {
        if(likes == null){
            return;
        }
        if(!likes.contains(username)){
            return;
        }
        likes = new HashSet<>(likes);
        likes.remove(username);
    }

    public boolean addDislike(String username) {
        removeLikeIfExists(username);
        if (dislikes == null){
            this.dislikes = Set.of(username);
            return true;
        }
        if (dislikes.contains(username)) {
            return false;
        }
        Set<String> newDislikes = new HashSet<>(this.dislikes);
        newDislikes.add(username);
        this.dislikes = newDislikes;
        return true;
    }
}
