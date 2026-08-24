package com.dmytro.language_learning_api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dmytro.language_learning_api.dto.TranslationDTO;
import com.dmytro.language_learning_api.model.Translation;

@Mapper(componentModel = "spring")
public interface TranslationMapper {

    TranslationDTO toDto(Translation translation);

    @Mapping(target = "source", ignore = true)
    @Mapping(target = "word", ignore = true)
    Translation fromDto(TranslationDTO translationDTO);

    List<TranslationDTO> toDto(List<Translation> translation);

    List<Translation> fromDto(List<TranslationDTO> translationDTO);
}