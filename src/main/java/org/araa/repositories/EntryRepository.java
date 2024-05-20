package org.araa.repositories;

import org.araa.domain.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {
    Entry findByLink(String url);

    boolean existsByLink(String url);
}
