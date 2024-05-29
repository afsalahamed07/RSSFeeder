package org.araa.repositories;

import org.araa.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername( String username );

    Boolean existsByUsername( String username );

    @Modifying
    @Query( value = "INSERT INTO user_entry (user_id, entry_id) VALUES (:userId, :entryId)", nativeQuery = true )
    void addEntryToUser( @Param( "userId" ) UUID userId, @Param( "entryId" ) UUID entryId );

    @Modifying
    @Query( value = "INSERT INTO user_subscriptions (user_id, rss_id) VALUES (:userId, :rssId)", nativeQuery = true )
    void subscribeToRss( @Param( "userId" ) UUID userId, @Param( "rssId" ) UUID rssId );

    @Query( value = "SELECT EXISTS(SELECT 1 FROM user_entry WHERE user_id = :userId AND entry_id = :entryId)", nativeQuery = true )
    boolean existsByEntry( @Param( "userId" ) UUID userId, @Param( "entryId" ) UUID entryId );
}
