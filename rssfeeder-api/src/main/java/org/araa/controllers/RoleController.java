package org.araa.controllers;

import lombok.AllArgsConstructor;
import org.araa.domain.Role;
import org.araa.dto.RoleSaveDto;
import org.araa.services.RoleService;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping( "/api/v2/role" )
public class RoleController {
    private RoleService roleService;

    @GetMapping( "/{roleId}" )
    public Role getRole( @PathVariable() Long roleId ) {
        return roleService.getRole( roleId );
    }

    @GetMapping()
    public Role getRole( @RequestParam String type ) {
        return roleService.getRole( type );
    }

    @PostMapping
    public Role saveRole( @RequestBody RoleSaveDto roleSaveDto ) {
        return roleService.saveRole( roleSaveDto.getType() );
    }

}
