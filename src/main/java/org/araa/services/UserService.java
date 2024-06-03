package org.araa.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.araa.application.dto.UserRegistrationDto;
import org.araa.application.dto.UserRegistrationResponseDTO;
import org.araa.application.error.UserAlreadyExistError;
import org.araa.domain.Entry;
import org.araa.domain.RSS;
import org.araa.domain.Role;
import org.araa.domain.User;
import org.araa.repositories.UserRepository;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LogManager.getLogger( UserService.class );
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final RedisCacheManager redisCacheManager;

    private EntityManagerFactory entityManagerFactory; // this is sued to create transaction for async calls

    public UserRegistrationResponseDTO registerUser( UserRegistrationDto userRegistrationDto ) throws UserAlreadyExistError {
        User user = User.builder()
                .username( userRegistrationDto.getUsername() )
                .name( userRegistrationDto.getName() )
                .email( userRegistrationDto.getEmail() )
                .password( passwordEncoder.encode( userRegistrationDto.getPassword() ) )
                .createdDate( new Date() )
                .build();

        Role role = roleService.setUserRole( "USER" );
        user.setRole( role );

        user = save( user ); // throws UserAlreadyExistError

        return new UserRegistrationResponseDTO( user );
    }

    @Override
    public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException {
        User user = userRepository.findByUsername( username )
                .orElseThrow( () -> new UsernameNotFoundException( username + " not found" ) );

        return new org.springframework.security.core.userdetails.
                User( user.getUsername(), user.getPassword(), mapRolesToAuthorities( user.getRoles() ) );
    }


    @Async
    public void subscribeRSS( User user, RSS rss ) {
        if ( user.getSubscriptions().contains( rss ) ) {
            logger.info( "User {} already subscribed to RSS {}", user.getUsername(), rss.getUrl() );
            return;
        }
        try ( EntityManager entityManager = entityManagerFactory.createEntityManager() ) {
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery( "INSERT INTO user_subscriptions (user_id, rss_id) VALUES (:userId, :rssId)" )
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

    private List<SimpleGrantedAuthority> mapRolesToAuthorities( List<Role> roles ) {
        return roles.stream()
                .map( role -> new SimpleGrantedAuthority( "ROLE_" + role.getType() ) )
                .toList();
    }


    public Set<RSS> getUserSubscriptions( User user ) {
        return user.getSubscriptions();
    }

    public User getUserByUsername( String username ) {
        return userRepository.findByUsername( username )
                .orElseThrow( () -> new UsernameNotFoundException( "User not found" ) );
    }

    private User save( User user ) {
        if ( Boolean.TRUE.equals( userRepository.existsByUsername( user.getUsername() ) ) ) {
            throw new UserAlreadyExistError( "Username already exists" );
        }
        return userRepository.save( user );
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