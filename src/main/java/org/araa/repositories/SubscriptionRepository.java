package org.araa.repositories;

import org.araa.domain.Subscription;
import org.araa.infrastructure.SubscriptionID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionID>{
}
