package org.araa.repositories;

import org.araa.domain.RSS;
import org.araa.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername( String username );

    Boolean existsByUsername( String username );
}
