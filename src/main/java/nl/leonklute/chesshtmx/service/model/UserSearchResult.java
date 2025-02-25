package nl.leonklute.chesshtmx.service.model;

import nl.leonklute.chesshtmx.db.model.UserEntity;

import java.util.List;
import java.util.Objects;

public record UserSearchResult(String username, int rating, List<String> roles) {
    public static UserSearchResult from(UserEntity userEntity){
        return new UserSearchResult(userEntity.getUsername(),
                userEntity.getRating(),
                userEntity.getRoles().stream().map(Objects::toString).toList());
    }
}
