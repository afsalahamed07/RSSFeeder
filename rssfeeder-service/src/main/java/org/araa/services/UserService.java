package org.araa.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.araa.domain.Entry;
import org.araa.domain.RSS;
import org.araa.domain.Role;
import org.araa.domain.User;
import org.araa.error.UserAlreadyExistError;
import org.araa.repositories.UserRepository;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Service
public class UserService {

    private static final Logger logger = LogManager.getLogger( UserService.class );
    private final UserRepository userRepository;
    private final RedisCacheManager redisCacheManager;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    private EntityManagerFactory entityManagerFactory; // this is sued to create transaction for async calls

    public User registerUser( String userName, String name, String email, String password ) throws UserAlreadyExistError {
        User user = User.builder()
                .username( userName )
                .name( name )
                .email( email )
                .password( passwordEncoder.encode( password ) )
                .createdDate( new Date() )
                .build();


        setUseRole( user );
        user = save( user ); // throws UserAlreadyExistError

        return user;
    }

    private void setUseRole( User user ) {
        Role role = roleService.getRole( "USER" );
        user.setRole( role );
    }

    private User save( User user ) {
        if ( Boolean.TRUE.equals( userRepository.existsByUsername( user.getUsername() ) ) ) {
            throw new UserAlreadyExistError( "Username already exists" );
        }
        return userRepository.save( user );
    }

    @Async
    public void subscribeRSS( User user, RSS rss ) {
        if ( user.getSubscriptions().contains( rss ) ) {
            logger.info( "User {} already subscribed to RSS {}", user.getUsername(), rss.getUrl() );
            return;
        }

        try ( EntityManager entityManager = entityManagerFactory.createEntityManager() ) {
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery( "INSERT INTO user_subscriptions (user_id, rss_id) VALUES (:userId, " +
                            ":rssId)" )
                    .setParameter( "userId", user.getId() )
                    .setParameter( "rssId", rss.getId() )
                    .executeUpdate();
            entityManager.getTransaction().commit();
            logger.info( "User {} subscribed to RSS {}", user.getUsername(), rss.getUrl() );
        } catch ( Exception e ) {
            logger.error( "Failed to subscribe user to RSS", e );
        }

        Objects.requireNonNull( redisCacheManager.getCache( "entries" ) ).clear();
    }

    public Set<RSS> getUserSubscriptions( User user ) {
        return user.getSubscriptions();
    }

    public User getUserByUsername( String username ) {
        return userRepository.findByUsername( username )
                .orElseThrow( () -> new RuntimeException( "User not found" ) ); // todo: This should throw user not
        // found exception
    }

    @Async
    public void updateEntries( User user, CompletableFuture<List<Entry>> entries ) {
        entries.thenAccept( entriesList -> {
            try ( EntityManager em = entityManagerFactory.createEntityManager() ) {
                em.getTransaction().begin();
                for ( Entry entry : entriesList ) {
                    if ( userRepository.existsByEntry( user.getId(), entry.getId() ) ) {
                        logger.info( "Entry {} already saved to user {}", entry.getId(), user.getId() );
                        continue;
                    }
                    em.createNativeQuery( "INSERT INTO user_entry (user_id, entry_id) VALUES (:userId, :entryId)" )
                            .setParameter( "userId", user.getId() )
                            .setParameter( "entryId", entry.getId() )
                            .executeUpdate();

                    logger.info( "Entry {} saved to user {}", entry.getId(), user.getId() );
                }
                em.getTransaction().commit();
            } catch ( Exception e ) {
                logger.error( "Failed to save entries to user", e );
            }
        } );
    }
}