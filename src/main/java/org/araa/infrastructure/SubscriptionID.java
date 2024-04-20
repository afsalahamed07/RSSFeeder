package org.araa.infrastructure;

import jakarta.persistence.Embeddable;
import org.araa.domain.Profile;
import org.araa.domain.RSS;

import java.io.Serializable;

@Embeddable
public class SubscriptionID implements Serializable {
    private Profile profile;
    private RSS rss;
}
