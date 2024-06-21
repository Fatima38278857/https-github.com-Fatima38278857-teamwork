package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class AuthControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private AuthService authService;
    @InjectMocks
    private AuthController authController;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testRegisterUser_Success() throws Exception {
        Register register = new Register();
        register.setUsername("user");
        register.setPassword("password");
        register.setFirstName("John");
        register.setLastName("Doe");
        register.setPhone("+71234567890");
        when(authService.register(any(Register.class))).thenReturn(true);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testRegisterUser_Failure() throws Exception {
        Register register = new Register();
        register.setUsername("user");
        register.setPassword("password");
        register.setFirstName("John");
        register.setLastName("Doe");
        register.setPhone("+71234567890");

        when(authService.register(any(Register.class))).thenReturn(false);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLogin_Success() throws Exception {
        Login login = new Login();
        login.setUsername("user");
        login.setPassword("password");

        when(authService.login(login.getUsername(), login.getPassword())).thenReturn(true);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk());
    }

    @Test
    public void testLogin_Failure() throws Exception {
        Login login = new Login();
        login.setUsername("user");
        login.setPassword("password");

        when(authService.login("1", "2")).thenReturn(false);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized());
    }
}