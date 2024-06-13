//package org.araa.services;
//
//import org.araa.application.dto.UserRegistrationDto;
//import org.araa.application.dto.UserRegistrationResponseDTO;
//import org.araa.application.error.UserAlreadyExistError;
//import org.araa.domain.Role;
//import org.araa.domain.User;
//import org.araa.repositories.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//
//@ExtendWith( MockitoExtension.class )
//class UserServiceTest {
//    @Mock
//    UserRepository userRepository;
//
//    @Mock
//    PasswordEncoder passwordEncoder;
//
//    @Mock
//    RoleService roleService;
//
//    @InjectMocks
//    UserService userService;
//
//    @Test
//    void register_a_new_user() {
//        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
//        userRegistrationDto.setUsername( "testuser" );
//        userRegistrationDto.setEmail( "testuser@emial.com" );
//        userRegistrationDto.setName( "Test User" );
//        userRegistrationDto.setPassword( "password" );
//
//        when( passwordEncoder.encode( anyString() ) ).thenReturn( "encodedPassword" );
//        when( roleService.setUserRole( anyString() ) ).thenReturn( new Role() );
//        when( userRepository.existsByUsername( anyString() ) ).thenReturn( false );
//        when( userRepository.save( any( User.class ) ) ).thenReturn( new User() );
//
//        UserRegistrationResponseDTO userRegistrationResponseDTO = userService.registerUser( userRegistrationDto );
//
//        assertNotNull( userRegistrationResponseDTO );
//    }
//
//    @Test
//    void register_a_existing_user() {
//        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
//        userRegistrationDto.setUsername( "testuser" );
//        userRegistrationDto.setEmail( "testuser@emial.com" );
//        userRegistrationDto.setName( "Test User" );
//        userRegistrationDto.setPassword( "password" );
//
//        when( passwordEncoder.encode( anyString() ) ).thenReturn( "encodedPassword" );
//        when( roleService.setUserRole( anyString() ) ).thenReturn( new Role() );
//        when( userRepository.existsByUsername( anyString() ) ).thenReturn( true );
//
//        assertThrows( UserAlreadyExistError.class, () -> {
//            userService.registerUser( userRegistrationDto );
//        } );
//    }
//
//    @Test
//    void loadUserByUsername_for_existing_user() {
//        User mockUser = new User();
//        mockUser.setUsername( "testuser" );
//        mockUser.setPassword( "password" );
//        mockUser.setRole( new Role() );
//
//        when( userRepository.findByUsername( anyString() ) ).thenReturn( Optional.of( mockUser ) );
//
//        assertNotNull( userService.loadUserByUsername( "testuser" ) );
//    }
//
//    @Test
//    void loadUserByUsername_for_not_existing_user() {
//        when( userRepository.findByUsername( anyString() ) ).thenReturn( Optional.empty() );
//
//        assertThrows( UsernameNotFoundException.class, () -> {
//            userService.loadUserByUsername( "testuser" );
//        } );
//
//    }
//}