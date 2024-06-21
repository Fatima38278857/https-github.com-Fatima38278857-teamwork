package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.impl.ImageServiceImpl;

import static org.mockito.ArgumentMatchers.any;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageServiceImpl imageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void uploadImage() throws IOException {
        MultipartFile imageFile = new MockMultipartFile("image.jpg", new byte[1024]);

        Image image = new Image();
        image.setFileSize(imageFile.getSize());
        image.setMediaType(imageFile.getContentType());
        image.setData(imageFile.getBytes());

        Mockito.when(imageRepository.save(any(Image.class))).thenReturn(image);


        Image savedImage = imageService.uploadImage(imageFile);
        assertNotNull(savedImage);
        assertEquals(image.getFileSize(), savedImage.getFileSize());
        assertEquals(image.getMediaType(), savedImage.getMediaType());
    }

    @Test
    public void getImage() {
        Image image = new Image();
        long id = 1L;
        Mockito.when(imageRepository.findById(id)).thenReturn(Optional.of(image));


        Image getImage = imageService.getImage(id);
        assertNotNull(getImage);
        assertEquals(image, getImage);
    }
}