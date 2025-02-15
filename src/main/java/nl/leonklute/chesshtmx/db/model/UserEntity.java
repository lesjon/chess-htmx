package nl.leonklute.chesshtmx.db.model;

import java.security.Principal;

public class UserEntity implements Principal {
    private final String id;

    public UserEntity(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return id;
    }
}
