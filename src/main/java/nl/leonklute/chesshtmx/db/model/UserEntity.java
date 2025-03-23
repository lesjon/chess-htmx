package nl.leonklute.chesshtmx.db.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "users")
public class UserEntity implements Principal, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    private int rating;
    @Enumerated(EnumType.ORDINAL)
    private List<Roles> roles;

    public UserEntity(String username, String password, int rating, List<Roles> roles) {
        this.username = username;
        this.password = password;
        this.rating = rating;
        this.roles = List.copyOf(roles);
    }

    public UserEntity(String username, String password) {
        this(username, password, 400, List.of(Roles.ROLE_USER));
    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(Roles::toString).map(Authority::new).toList();
    }

    record Authority(String role) implements GrantedAuthority {
        @Override
        public String getAuthority() {
            return role;
        }
    }

    @OneToOne(mappedBy = "user")
    PuzzleCollectionEntity puzzleCollectionEntity;

}
