package org.araa.repositories;

import org.araa.domain.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EntryRepository extends JpaRepository<Entry, UUID> {
    Entry findByLink(String url);

    boolean existsByLink(String url);
}
