package com.test.gcp.controller;

import com.test.gcp.exception.ResourceFoundException;
import com.test.gcp.payload.AdminDTO;
import com.test.gcp.payload.Role;
import com.test.gcp.security.JwtTokenProvider;
import com.test.gcp.service.EmployeeService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) 
@TestMethodOrder(OrderAnnotation.class) 
@WebMvcTest(value = AuthController.class) 
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

  @MockBean
  private AuthenticationManager authenticationManager;

  @MockBean
  private PasswordEncoder passwordEncoder;

  @MockBean
  private JwtTokenProvider tokenProvider;

  @InjectMocks
  private AuthController authController;

  @Autowired
  private MockMvc mvc;

  @BeforeEach
  void setUp() throws Exception {
  }

//  @Test
//  void testLoginAdmin() {
//    LoginDTO loginDto = new LoginDTO();
//    loginDto.setPassword("password");
//    loginDto.setEmail("testuser@test.com");
//
//    Authentication authentication = mock(Authentication.class);
//    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
//
//    when(tokenProvider.generateToken(authentication)).thenReturn("test-token");
//
//    ResponseEntity<JWTAuthResponse> response = authController.loginAdmin(loginDto);
//
//    assertEquals("test-token", response.getBody().getAccessToken());
//    assertEquals(HttpStatus.OK, response.getStatusCode());
//  }

  @Test @Order(1)
  void testRegisterAdmin() {
    AdminDTO adminDTO = new AdminDTO();
    adminDTO.setEmail("testuser@test.com");
    adminDTO.setName("Test User");
    adminDTO.setPassword("password");
    adminDTO.setAddress("testaddress");
    Role role = new Role();
    role.setName("ROLE_ADMIN");
    when(passwordEncoder.encode(adminDTO.getPassword())).thenReturn("hashed-password");
    ResponseEntity<String> response = authController.registerAdmin(adminDTO);
    assertEquals("Admin registered successfully", response.getBody());
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
  }

  @Test @Order(2)
  void testRegisterAdmin_ResourceFoundException() {
    AdminDTO adminDTO = new AdminDTO();
    adminDTO.setEmail("testuser@test.com");
    adminDTO.setName("Test User");
    adminDTO.setPassword("password");
    adminDTO.setAddress("testaddress");
    Role role = new Role();
    role.setName("ROLE_ADMIN");
    Throwable throwable = assertThrows(ResourceFoundException.class,
        () -> authController.registerAdmin(adminDTO));
    assertEquals("Email already found with : 'testuser@test.com'", throwable.getMessage());
  }

  // Negative Scenarios
  @Test @DisplayName("Testing for successful User and Admin registration") @Order(3)
  void testSuccessAdmin() throws Exception {

    String input = "{\r\n  \"name\": \"Pavani\",\r\n  \"password\": \"Pavani20@\",\r\n  \"email\": \"k.pavani@gmail.com\",\r\n  \"address\": \"Andhra Pradesh\",\r\n  \"roles\": [\r\n    {\r\n      \"name\": \"ROLE_ADMIN\"\r\n    }\r\n  ]\r\n}";
    MvcResult result = mvc
        .perform(MockMvcRequestBuilders.post("/api/auth/signup").content(input)
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andReturn();

    System.out.println(result.getResponse().getContentAsString());
    String expected = "Admin registered successfully";
    Assertions.assertEquals(201, result.getResponse().getStatus());
    Assertions.assertEquals(expected, result.getResponse().getContentAsString());
  }

  @Test @DisplayName("Testing for All Fields Blank") @Order(4)
  void testAllFieldsBlack() throws Exception {

    String input = "{\r\n  \"name\": \"\",\r\n  \"password\": \"\",\r\n  \"email\": \"\",\r\n  \"address\": \"\",\r\n  \"roles\": [\r\n    {\r\n      \"name\": \"\"\r\n    }\r\n  ]\r\n}";
    MvcResult result = mvc
        .perform(MockMvcRequestBuilders.post("/api/auth/signup").content(input)
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andReturn();

    System.out.println(result.getResponse());
    Assertions.assertEquals(400, result.getResponse().getStatus());
    assertTrue(
        result.getResponse().getContentAsString().contains("Name should not be null or empty"));
    assertTrue(
        result.getResponse().getContentAsString().contains("Email should not be null or empty"));
    assertTrue(
        result.getResponse().getContentAsString().contains("Password should not be null or empty"));
    assertTrue(result.getResponse().getContentAsString()
        .contains("Address must be between 10 to 50 characters"));
    assertTrue(result.getResponse().getContentAsString()
        .contains("Role Name should not be null or empty"));
    assertTrue(result.getResponse().getContentAsString()
        .contains("Role Name should be ROLE_ADMIN or ROLE_USER"));

  }

  @Test @DisplayName("Testing for existing mail") @Order(5)
  void testExistingMail() throws Exception {
    AdminDTO signUpDto = new AdminDTO();
    signUpDto.setName("Pavani");
    signUpDto.setPassword("Pavani");
    signUpDto.setEmail("k.pavani@gmail.com");
    signUpDto.setAddress("Andhra Pradesh");
    Set<Role> role = new HashSet<>();
    Role myRole = new Role();
    myRole.setName("ROLE_ADMIN");
    role.add(myRole);
    signUpDto.setRoles(role);
    EmployeeService.ADMINS.add(signUpDto);

    String input = "{\r\n  \"name\": \"Pavani\",\r\n  \"password\": \"Pavani20@\",\r\n  \"email\": \"k.pavani@gmail.com\",\r\n  \"address\": \"Andhra Pradesh\",\r\n  \"roles\": [\r\n    {\r\n      \"name\": \"ROLE_ADMIN\"\r\n    }\r\n  ]\r\n}";
    MvcResult result = mvc
        .perform(MockMvcRequestBuilders.post("/api/auth/signup").content(input)
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andReturn();

    System.out.println(result.getResponse().getContentAsString());
    Assertions.assertEquals(409, result.getResponse().getStatus());
    assertTrue(result.getResponse().getContentAsString().contains("Email already found"));
  }

  @Test @DisplayName("Testing for successful Signin") @Order(6)
  void testSuccessSignIn() throws Exception {

    String input = "{\r\n  \"password\": \"pavani20@\",\r\n  \"email\": \"k.pavani@gmail.com\"\r\n}";
    MvcResult result = mvc
        .perform(MockMvcRequestBuilders.post("/api/auth/signin").content(input)
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andReturn();

    System.out.println(result.getResponse().getContentAsString());
    Assertions.assertEquals(200, result.getResponse().getStatus());
    assertTrue(result.getResponse().getContentAsString().contains("Bearer"));
  }

  @Test @DisplayName("Testing for empty fields Signin") @Order(7)
  void testEmptySignIn() throws Exception {

    String input = "{\r\n  \"password\": \"\",\r\n  \"email\": \"\"\r\n}";
    MvcResult result = mvc
        .perform(MockMvcRequestBuilders.post("/api/auth/signin").content(input)
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andReturn();

    System.out.println(result.getResponse().getContentAsString());
    Assertions.assertEquals(400, result.getResponse().getStatus());
    assertTrue(
        result.getResponse().getContentAsString().contains("Email should not be null or empty"));
    assertTrue(
        result.getResponse().getContentAsString().contains("Password should not be null or empty"));

  }
}
