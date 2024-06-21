package ru.skypro.homework.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.mock.web.MockMultipartFile;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.impl.ImageServiceImpl;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@DataJpaTest
public class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageServiceImpl imageService;

    @Test
    public void testUploadImage() throws IOException {
        // Prepare a mock MultipartFile
        MockMultipartFile file = new MockMultipartFile("image", "test-image.jpg", "image/jpeg", "test image content".getBytes());

        // Prepare a mock Image entity
        Image mockImage = new Image();
        mockImage.setId(1L);
        mockImage.setFileSize(file.getSize());
        mockImage.setMediaType(file.getContentType());
        mockImage.setData(file.getBytes());

        // Mock the imageRepository.save method to return the mock Image entity
        when(imageRepository.save(any(Image.class))).thenReturn(mockImage);

        // Call the uploadImage method
        Image uploadedImage = imageService.uploadImage(file);

        // Assertions
        assertEquals(mockImage.getId(), uploadedImage.getId());
        assertEquals(mockImage.getFileSize(), uploadedImage.getFileSize());
        assertEquals(mockImage.getMediaType(), uploadedImage.getMediaType());
        assertEquals(mockImage.getData(), uploadedImage.getData());
    }

    // Add more test cases as needed for other methods in ImageServiceImpl
}