package com.test.gcp.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.test.gcp.payload.AdminDTO;
import com.test.gcp.payload.Role;
import com.test.gcp.service.EmployeeService;

@ExtendWith(MockitoExtension.class) @TestMethodOrder(OrderAnnotation.class)
class CustomUserDetailsServiceTest {

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private AdminDTO adminDTO;

    @BeforeEach
    void setUp() throws Exception {
        adminDTO = new AdminDTO();
        adminDTO.setEmail("avirup.pal@gmail.com");
        adminDTO.setPassword("password");
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        adminDTO.setRoles(roles);
    }

    @Test @Order(1)
    void testLoadUserByUsername() {
        EmployeeService.ADMINS.add(adminDTO);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("avirup.pal@gmail.com");
        assertEquals("avirup.pal@gmail.com", userDetails.getUsername());
    }

    @Test @Order(2)
    void testLoadUserByUsername_UsernameNotFoundException1() {
        Throwable throwable = assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("avirup.pal1@gmail.com"));
        assertEquals("Admin not found with email:avirup.pal1@gmail.com", throwable.getMessage());
    }

    @Test @Order(3)
    void testLoadUserByUsername_UsernameNotFoundException2() {
        EmployeeService.ADMINS.add(adminDTO);
        Throwable throwable = assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("avirup.pal1@gmail.com"));
        assertEquals("Admin not found with email:avirup.pal1@gmail.com", throwable.getMessage());
    }
}
