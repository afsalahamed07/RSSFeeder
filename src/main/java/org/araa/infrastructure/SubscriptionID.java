package org.araa.infrastructure;

import jakarta.persistence.Embeddable;
import org.araa.domain.User;
import org.araa.domain.RSS;

import java.io.Serializable;

@Embeddable
public class SubscriptionID implements Serializable {
    private User user;
    private RSS rss;
}
