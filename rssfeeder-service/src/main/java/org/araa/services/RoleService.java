package org.araa.services;

import lombok.AllArgsConstructor;
import org.araa.domain.Role;
import org.araa.repositories.RoleRepository;
import org.hibernate.FetchNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@AllArgsConstructor
@Service
public class RoleService {
    private RoleRepository roleRepository;

    public Role getRole( Long roleId ) { return roleRepository.findById( roleId ).orElseThrow( () -> new FetchNotFoundException( "Role", roleId ) );
    }

    public Role getRole( String type ) {
        return roleRepository.findByType( type ).orElseThrow( () -> new FetchNotFoundException( "Role", type ) );
    }

    public Role saveRole( String type ) {
        Role role = Role.builder()
                .type( type )
                .createdDate( new Date() )
                .build();
        return roleRepository.save( role );
    }

    public Role setUserRole( String roleType ) {
        return getRole( Objects.requireNonNullElse( roleType, "USER" ) );
    }
}
