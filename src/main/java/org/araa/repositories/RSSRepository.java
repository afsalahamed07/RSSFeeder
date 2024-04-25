package org.araa.repositories;

import org.araa.domain.RSS;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RSSRepository extends JpaRepository<RSS, Long> {
}
