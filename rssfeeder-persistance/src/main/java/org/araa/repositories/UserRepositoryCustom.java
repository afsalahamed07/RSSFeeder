package org.araa.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.araa.domain.RSS;
import org.araa.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Repository
public class UserRepositoryCustom {
    private static final Logger logger = LogManager.getLogger( UserRepositoryCustom.class );

    private final EntityManagerFactory entityManagerFactory;

    public void subscribe( User user, RSS rss ) {
        try ( EntityManager entityManager = entityManagerFactory.createEntityManager() ) {
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery( "INSERT INTO user_subscriptions (user_id, rss_id) " +
                            "VALUES " + "(:userId, " + ":rssId)" )
                    .setParameter( "userId", user.getId() )
                    .setParameter( "rssId", rss.getId() )
                    .executeUpdate();
            entityManager.getTransaction().commit();
            logger.info( "User {} subscribed to RSS {}", user.getUsername(), rss.getUrl() );
        } catch ( Exception e ) {
            logger.error( "Failed to subscribe user to RSS", e );
        }
    }

    public void unsubscribe( User user, RSS rss ) {
        try ( EntityManager entityManager = entityManagerFactory.createEntityManager() ) {
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery( "DELETE FROM user_subscriptions " +
                            "WHERE user_id = :userId AND rss_id = :rssId" )
                    .setParameter( "userId", user.getId() )
                    .setParameter( "rssId", rss.getId() )
                    .executeUpdate();
            entityManager.getTransaction().commit();
            logger.info( "User {} unsubscribed from RSS {}", user.getUsername(), rss.getUrl() );
        } catch ( Exception e ) {
            logger.error( "Failed to unsubscribe user from RSS", e );
        }
    }

    public List<RSS> getSubscriptions(User user) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            List<?> results = entityManager.createNativeQuery(
                            "SELECT r.* FROM rss r " +
                                    "JOIN user_subscriptions us ON r.rss_id = us.rss_id " +
                                    "WHERE us.user_id = :userId", RSS.class)
                    .setParameter("userId", user.getId())
                    .getResultList();

            return results.stream()
                    .map( RSS.class::cast)
                    .toList();
        }
    }


    public boolean isSubscribed(User user, RSS rss) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            Number count = (Number) entityManager.createNativeQuery(
                            "SELECT COUNT(*) FROM user_subscriptions " +
                                    "WHERE user_id = :userId AND rss_id = :rssId")
                    .setParameter("userId", user.getId())
                    .setParameter("rssId", rss.getId())
                    .getSingleResult();

            return count.intValue() > 0;
        }
    }
}
