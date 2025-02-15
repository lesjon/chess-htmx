package nl.leonklute.chesshtmx.service;

import nl.leonklute.chesshtmx.db.UserRepository;
import nl.leonklute.chesshtmx.db.model.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserEntity createUser(String username){
        var user = new UserEntity(username);
        repository.save(user);
        return user;
    }
}
