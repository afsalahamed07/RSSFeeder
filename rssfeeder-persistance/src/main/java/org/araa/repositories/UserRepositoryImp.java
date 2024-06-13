package org.araa.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.araa.domain.RSS;
import org.araa.domain.User;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Repository
public class UserRepositoryImp {
    private static final Logger logger = LogManager.getLogger( UserRepositoryImp.class );

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

}
