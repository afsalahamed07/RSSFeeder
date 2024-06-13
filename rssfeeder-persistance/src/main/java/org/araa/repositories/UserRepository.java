package org.araa.repositories;

import org.araa.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername( String username );

    Boolean existsByUsername( String username );

    @Query( value = "SELECT EXISTS(SELECT 1 FROM user_entry WHERE user_id = :userId AND entry_id = :entryId)",
            nativeQuery = true )
    boolean existsByEntry( @Param( "userId" ) UUID userId, @Param( "entryId" ) UUID entryId );
}
