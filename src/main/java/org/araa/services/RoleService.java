package org.araa.services;

import lombok.AllArgsConstructor;
import org.araa.application.dto.RoleSaveDto;
import org.araa.domain.Role;
import org.araa.repositories.RoleRepository;
import org.hibernate.FetchNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
public class RoleService {
    private RoleRepository roleRepository;

    public Role getRole( Long roleId ) {
        return roleRepository.findById( roleId ).orElseThrow( () -> new FetchNotFoundException("Role", roleId ) );
    }

    public Role getRole( String type ) {
        return roleRepository.findByType( type ).orElseThrow( () -> new FetchNotFoundException("Role", type ) );
    }

    public Role saveRole( RoleSaveDto roleSaveDto ) {
        Role role = new Role();
        role.setType( roleSaveDto.getType() );
        role.setCreatedDate( new Date() );
        return roleRepository.save( role );
    }
}