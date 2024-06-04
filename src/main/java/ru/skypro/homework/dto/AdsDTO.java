package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "Ads")
public class AdsDTO {
    @Schema(description = "общее количество объявлений")
    private int count;
    private List<AdDTO> result;
}
