package org.araa.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.araa.infrastructure.SubscriptionID;

@Entity
@Table(name = "subscriptions")
@IdClass(SubscriptionID.class)
@Data
public class Subscription {

    @Id
    @ManyToOne
    @JoinColumn(name = "username")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "rssID")
    private RSS rss;
}

