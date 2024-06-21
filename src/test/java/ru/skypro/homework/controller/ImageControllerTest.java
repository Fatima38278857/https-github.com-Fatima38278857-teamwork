package ru.skypro.homework.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.impl.ImageServiceImpl;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageControllerTest {

    @Mock
    private ImageServiceImpl imageService;

    @InjectMocks
    private ImageController imageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void uploadImage() throws IOException {

        MultipartFile mockImage = new MockMultipartFile("image.jpg", new byte[1024]);


        ResponseEntity<String> response = imageController.uploadImage(mockImage);

        verify(imageService, times(1)).uploadImage(mockImage);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void downloadImage() {

        long imageId = 1L;
        Image mockImage = new Image();
        mockImage.setData(new byte[]{1, 2, 3, 4, 5});
        mockImage.setMediaType("image/jpeg");

        when(imageService.getImage(imageId)).thenReturn(mockImage);


        ResponseEntity<byte[]> response = imageController.downloadImage(imageId);

        verify(imageService, times(1)).getImage(imageId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertArrayEquals(mockImage.getData(), response.getBody());
    }

    @Test
    void downloadImageNotFound() {
        long imageId = 2L;

        when(imageService.getImage(imageId)).thenReturn(null);


        ResponseEntity<byte[]> response = imageController.downloadImage(imageId);

        verify(imageService, times(1)).getImage(imageId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
