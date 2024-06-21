package ru.skypro.homework.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.security.MyUserDetailsService;
import ru.skypro.homework.service.impl.AuthServiceImpl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthServiceTest {

    @Mock
    private MyUserDetailsService myUserDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    public void testLogin_validCredentials() {
        // Mock data
        String username = "test@example.com";
        String password = "password";
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        when(myUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(passwordEncoder.matches(password, userDetails.getPassword())).thenReturn(true);

        // Test login
        boolean result = authService.login(username, password);

        // Verify interactions and assertions
        assertTrue(result);
        verify(myUserDetailsService, times(1)).loadUserByUsername(username);
        verify(passwordEncoder, times(1)).matches(password, userDetails.getPassword());
    }

    @Test
    public void testLogin_invalidCredentials() {
        // Mock data
        String username = "test@example.com";
        String password = "password";
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        when(myUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(passwordEncoder.matches(password, userDetails.getPassword())).thenReturn(false);

        // Test login
        boolean result = authService.login(username, password);

        // Verify interactions and assertions
        assertFalse(result);
        verify(myUserDetailsService, times(1)).loadUserByUsername(username);
        verify(passwordEncoder, times(1)).matches(password, userDetails.getPassword());
    }

    @Test
    public void testRegister_successful() {
        // Mock data
        Register register = new Register();
        register.setUsername("test@example.com");
        register.setPassword("password");
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(register.getUsername());
        when(userService.saveNewUser(any())).thenReturn(userEntity);

        // Test registration
        boolean result = authService.register(register);

        // Verify interactions and assertions
        assertTrue(result);
        verify(userMapper, times(1)).toUser(register);
        verify(userService, times(1)).saveNewUser(any());
    }

    @Test
    public void testRegister_unsuccessful() {
        // Mock data
        Register register = new Register();
        register.setUsername("test@example.com");
        register.setPassword("password");
        when(userService.saveNewUser(any())).thenReturn(null);

        // Test registration
        boolean result = authService.register(register);

        // Verify interactions and assertions
        assertFalse(result);
        verify(userMapper, times(1)).toUser(register);
        verify(userService, times(1)).saveNewUser(any());
    }
}