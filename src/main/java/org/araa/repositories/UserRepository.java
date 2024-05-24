package org.araa.repositories;

import jakarta.persistence.LockModeType;
import org.araa.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Lock( LockModeType.PESSIMISTIC_WRITE )
    Optional<User> findByUsername( String username );

    Boolean existsByUsername( String username );
}
