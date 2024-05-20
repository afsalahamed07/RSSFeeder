package org.araa.repositories;

import org.araa.domain.Entry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EntryRepository extends JpaRepository<Entry, UUID> {
    Entry findByLink( String url );

    boolean existsByLink( String url );

    @Query( "SELECT e FROM User u JOIN u.entries e WHERE u.userId = :userId ORDER BY e.publishedDate ASC" )
    Page<Entry> findEntriesByUserId( @Param( "userId" ) UUID userId, Pageable pageable );
}
