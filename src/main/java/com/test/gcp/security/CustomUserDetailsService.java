package com.test.gcp.security;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.test.gcp.payload.AdminDTO;
import com.test.gcp.payload.Role;
import com.test.gcp.service.EmployeeService;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {

        AdminDTO presentAdminDTO = null;
        if (EmployeeService.ADMINS.isEmpty()) {
            throw new UsernameNotFoundException("Admin not found with email:" + email);
        } else {
            Optional<AdminDTO> adminDTO = EmployeeService.ADMINS.stream().filter(e -> e.getEmail().equalsIgnoreCase(email)).findFirst();
            if (adminDTO.isPresent()) {
                presentAdminDTO = adminDTO.get();
            } else {
                throw new UsernameNotFoundException("Admin not found with email:" + email);
            }
        }

        return new org.springframework.security.core.userdetails.User(presentAdminDTO.getEmail(), presentAdminDTO.getPassword(), mapRolesToAuthorities(presentAdminDTO.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(final Set<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();
    }
}
