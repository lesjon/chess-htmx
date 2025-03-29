package nl.leonklute.chesshtmx.service;

import nl.leonklute.chesshtmx.db.UserRepository;
import nl.leonklute.chesshtmx.db.model.UserEntity;
import nl.leonklute.chesshtmx.service.model.UserSearchResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
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

    public Optional<Principal> getPrincipal() {
        return Optional.ofNullable((Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    public List<UserSearchResult> findUsers(String search) {
        return repository.findAllByUsernameContains(PageRequest.of(0, 100), search).stream()
                .map(UserSearchResult::from).toList();
    }

    public void update(UserEntity user) {
        repository.save(user);
    }

    public long getUserIdByPrincipal(Principal principal) {
        UserEntity user = getUserByPrincipal(principal);
        return user.getId();
    }

    public UserEntity getUserByPrincipal(Principal principal) {
        return repository.findByUsername(principal.getName()).orElseThrow(() -> new RuntimeException("Logged in user not in database"));
    }
}
