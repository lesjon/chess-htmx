package nl.leonklute.chesshtmx.service;

import nl.leonklute.chesshtmx.db.UserRepository;
import nl.leonklute.chesshtmx.db.model.UserEntity;
import nl.leonklute.chesshtmx.service.model.UserSearchResult;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity createUser(String username, String password) {
        var user = new UserEntity(username, passwordEncoder.encode(password));
        repository.save(user);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public Optional<UserEntity> getUserById(long id) {
        return repository.findById(id);
    }

    public List<UserSearchResult> findUsers(String search) {
        return repository.findByUsernameLike(search).stream()
                .map(UserSearchResult::from).toList();
    }
}
