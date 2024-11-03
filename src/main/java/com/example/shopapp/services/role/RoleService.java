package com.example.shopapp.services.role;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.shopapp.models.Role;
import com.example.shopapp.repositorys.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService{
	
    private final RoleRepository roleRepository;
    
    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

}
