package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.ExtendedAdDTO;
import ru.skypro.homework.service.AdService;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("ads")
public class AdController {

    private final AdService adService;

    @Operation(tags = "Объявления", summary = "Получение всех объявлений", responses = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AdsDTO.class)))})
    @GetMapping
    // Получение всех объявлений
    public ResponseEntity<AdsDTO> getAllAds() {
        return ResponseEntity.ok(adService.getAllAds());
    }

    @Operation(tags = "Объявления", summary = "Добавление объявление", responses = {@ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AdDTO.class))), @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // Добавление объявления. required: image, properties, status - 201 Created или 401 unauthorized
    // Примечание: не уверен в корректности authentication, надо будет проверить
    public ResponseEntity<AdDTO> addAd(@RequestPart CreateOrUpdateAdDTO properties, @RequestPart MultipartFile image, Authentication authentication) {
        log.info("addAd in AdController is used");
        if (authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(adService.addAd(image, properties, authentication.getName()));
        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Operation(tags = "Объявления", summary = "Получение информации об объявлении", responses = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ExtendedAdDTO.class))), @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()), @ApiResponse(responseCode = "404", description = "Not found", content = @Content())})
    @GetMapping("{id}")
    // Получение информации об объявлении (возможные ответы 200, 401, 404)
    public ResponseEntity<ExtendedAdDTO> getAds(@PathVariable int id, Authentication authentication) {
        log.info("getAds in AdController is used");
        if (authentication.isAuthenticated()) {
            if (adService.getAds(id) == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                return ResponseEntity.ok(adService.getAds(id));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(tags = "Объявления", summary = "Удаление объявления",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content", content = @Content()),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content()),
                    @ApiResponse(responseCode = "403", content = @Content)})
    @PreAuthorize("hasRole('ADMIN') or @adServiceImpl.findAdById(id).author.email.equals(authentication.name)")
    @DeleteMapping("{id}")
    // Удаление объявления (возможные ответы 204 No Content, 401 Unauthorized, 404 Not found)
    public ResponseEntity<Optional> removeAd(@PathVariable int id, Authentication authentication) {
        log.info("removeAd in AdController is used");
        if (authentication.isAuthenticated()) {
            if (adService.getAds(id) == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                adService.removeAd(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(tags = "Объявления", summary = "Обновление информации об объявлении",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AdDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content()),
                    @ApiResponse(responseCode = "403", content = @Content)})
    @PreAuthorize("hasRole('ADMIN') or adServiceImpl.findAdById(id).author.email.equals(authentication.name)")
    @PatchMapping("{id}")
    // Обновление информации об объявлении
    public ResponseEntity<AdDTO> updateAds(@PathVariable int id, @RequestBody CreateOrUpdateAdDTO properties, Authentication authentication) {
        log.info("updateAds in AdController is used");
        if (authentication.isAuthenticated()) {
            if (adService.getAds(id) == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                return ResponseEntity.ok(adService.updateAds(id, properties));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(tags = "Объявления", summary = "Получение объявлений авторизованного пользователя", responses = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AdsDTO.class))), @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())})
    @PreAuthorize("hasRole('USER')")
    @GetMapping("me")
    // Получение объявлений авторизованного пользователя (200 или 401)
    public ResponseEntity<AdsDTO> getAdsMe(Authentication authentication) {
        log.info("getAdsMe in AdController is used");
        if (authentication.isAuthenticated()) {
            return ResponseEntity.ok(adService.getAdsMe());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(tags = "Объявления", summary = "Обновление картинки объявления", responses = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE, array = @ArraySchema(schema = @Schema(implementation = byte[].class)))), @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()), @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()), @ApiResponse(responseCode = "404", description = "Not found", content = @Content())})
    @PreAuthorize("hasRole('ADMIN') or adServiceImpl.findAdById(id).author.email.equals(authentication.name)")
    @PatchMapping(value = "{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // Обновление картинки объявления (статусы 200, 403, 401, 404)
    public ResponseEntity<String[]> updateImage(@PathVariable int id, @RequestParam MultipartFile image, Authentication authentication) {
        log.info("updateImage in AdController is used");
        if (authentication.isAuthenticated()) {
            if (adService.getAds(id) == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                return ResponseEntity.ok(adService.updateImage(id, image));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
