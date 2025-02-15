package nl.leonklute.chesshtmx.db;

import nl.leonklute.chesshtmx.db.model.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {
    private final List<UserEntity> users;

    public UserRepository() {
        this.users = new ArrayList<>();
    }


    public void save(UserEntity user) {
        users.add(user);
    }
}
