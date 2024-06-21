package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper mapper;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testSetPassword_Success() {
        NewPassword newPassword = new NewPassword("currentPassword", "newPassword123");
        UserEntity user = new UserEntity();
        user.setPassword("encodedPassword");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(userRepository.findByEmailIgnoreCase("user@example.com")).thenReturn(user);
        when(encoder.matches(eq("currentPassword"), any())).thenReturn(true);
        when(encoder.encode("newPassword123")).thenReturn("encodedNewPassword");

        boolean result = userService.setPassword(newPassword);

        assertTrue(result);
        assertEquals("encodedNewPassword", user.getPassword());
    }

    @Test
    public void testSetPassword_Failure() {
        NewPassword newPassword = new NewPassword("currentPassword", "newPassword123");
        UserEntity user = new UserEntity();
        user.setPassword("encodedPassword");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(userRepository.findByEmailIgnoreCase("user@example.com")).thenReturn(user);
        when(encoder.matches(eq("currentPassword"), any())).thenReturn(false);

        boolean result = userService.setPassword(newPassword);

        assertFalse(result);
    }

    @Test
    public void testGetUser_Success() {
        UserEntity user = new UserEntity();
        UserDto userDto = new UserDto();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(userRepository.findByEmailIgnoreCase("user@example.com")).thenReturn(user);
        when(mapper.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.getUser();

        assertEquals(userDto, result);
    }

    @Test
    public void testGetUser_NotFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(userRepository.findByEmailIgnoreCase("user@example.com")).thenReturn(null);

        assertThrows(RuntimeException.class, () -> userService.getUser());
    }

    @Test
    public void testUpdateUser_Success() {
        UpdateUser updateUser = new UpdateUser();
        updateUser.setFirstName("John");
        updateUser.setLastName("Doe");
        updateUser.setPhone("+71234567890");

        UserEntity user = new UserEntity();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(userRepository.findByEmailIgnoreCase("user@example.com")).thenReturn(user);
        when(mapper.toUpdateUser(user)).thenReturn(updateUser);

        UpdateUser result = userService.updateUser(updateUser);

        assertEquals(updateUser, result);
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("+71234567890", user.getPhone());
    }

    @Test
    public void testUpdateUserImage_Success() throws IOException {
        MultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", "test image".getBytes());
        UserEntity user = new UserEntity();
        Image newImage = new Image();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(userRepository.findByEmailIgnoreCase("user@example.com")).thenReturn(user);
        when(imageService.uploadImage(image)).thenReturn(newImage);

        userService.updateUserImage(image);

        assertEquals(newImage, user.getImage());
    }

    @Test
    public void testUpdateUserImage_ThrowsIOException() throws IOException {
        MultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", (byte[]) null);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(userRepository.findByEmailIgnoreCase("user@example.com")).thenReturn(new UserEntity());
        when(imageService.uploadImage(image)).thenThrow(IOException.class);

        assertThrows(IOException.class, () -> userService.updateUserImage(image));
    }
}