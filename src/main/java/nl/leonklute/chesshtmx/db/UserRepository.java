package nl.leonklute.chesshtmx.db;

import nl.leonklute.chesshtmx.db.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long>, CrudRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    Page<UserEntity> findAllByUsernameContains(Pageable pageable, String search);

}
