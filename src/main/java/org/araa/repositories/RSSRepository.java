package org.araa.repositories;

import org.araa.domain.RSS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RSSRepository extends JpaRepository<RSS, Long> {
    boolean existsByUrl(String url);

    RSS findByUrl( String url );
}
