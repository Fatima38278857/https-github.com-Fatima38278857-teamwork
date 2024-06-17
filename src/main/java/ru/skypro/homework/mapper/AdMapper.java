package ru.skypro.homework.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.model.AdEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AdMapper {
    AdDTO toDTO(AdEntity adEntity);
    AdEntity toAd(AdDTO adDTO);

}