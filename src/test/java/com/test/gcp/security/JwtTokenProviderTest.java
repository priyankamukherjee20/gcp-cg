package com.test.gcp.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import com.test.gcp.exception.BlogAPIException;

import io.jsonwebtoken.security.SignatureException;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private Authentication authentication;

    private String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdmlydXAucGFsQGdtYWlsLmNvbSIsImlhdCI6MTY3ODkwOTIyMCwiZXhwIjoxNjc5NTE0MDIwfQ.EaquIvz6qQSNnlJxLgVJkjOR3BrU4XjWklEpv24m2H5aEiE56rbYmW55y9869YYlaNhiXn3le1bAPZDfYBQcqg";

    @BeforeEach
    void setUp() throws Exception {
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", "JWTSecretKey1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationInMs", 604800000);
    }

    @Test
    void testGenerateToken() {
        when(authentication.getName()).thenReturn("avirup.pal@gmail.com");
        jwtTokenProvider.generateToken(authentication);
    }

    @Test
    void testGetUsernameFromJWT() {
        String result = jwtTokenProvider.getUsernameFromJWT(token);
        assertEquals("avirup.pal@gmail.com", result);
    }

    @Test
    void testValidateToken() {
        boolean result = jwtTokenProvider.validateToken(token);
        assertTrue(result);
    }

    @Test
    void testValidateToken_BlogAPIException() {
        token = "eyJhbGciOiJIUzUxMi.eyJzdWIiOiJhdmlydXAucGFsQGdtYWlsLmNvbSIsImlhdCI6MTY3ODkwOTIyMCwiZXhwIjoxNjc5NTE0MDIwfQ.EaquIvz6qQSNnlJxLgVJkjOR3BrU4XjWklEpv24m2H5aEiE56rbYmW55y9869YYlaNhiXn3le1bAPZDfYBQcqg";
        Throwable throwable = assertThrows(BlogAPIException.class, () -> jwtTokenProvider.validateToken(token));
        assertEquals("Invalid JWT token", throwable.getMessage());
    }

    @Test
    void testValidateToken_SignatureException() {
        token = "eyJhbGciOiJIUzUxMiJ9.JzdWIiOiJhdmlydXAucGFsQGdtYWlsLmNvbSIsImlhdCI6MTY3ODkwOTIyMCwiZXhwIjoxNjc5NTE0MDIwfQ.EaquIvz6qQSNnlJxLgVJkjOR3BrU4XjWklEpv24m2H5aEiE56rbYmW55y9869YYlaNhiXn3le1bAPZDfYBQcqg";
        Throwable throwable = assertThrows(SignatureException.class, () -> jwtTokenProvider.validateToken(token));
        assertEquals("JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.", throwable.getMessage());
    }
}
