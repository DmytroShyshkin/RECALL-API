package com.dmytro.language_learning_api.mapper;

import com.dmytro.language_learning_api.dto.WordsDTO;
import com.dmytro.language_learning_api.model.Words;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {TranslationMapper.class})
public interface WordsMapper {

    @Mapping(target = "synonymIds", ignore = true)
    @Mapping(target = "translations", source = "translations")
    WordsDTO toDto(Words word);

    default Set<UUID> synonymsToIds(Set<Words> synonyms) {
        if (synonyms == null) return Collections.emptySet();
        return synonyms.stream()
                .map(Words::getId)
                .collect(Collectors.toSet());
    }

    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "translations", ignore = true)
    @Mapping(target = "synonyms", ignore = true)
    Words fromDto(WordsDTO dto);

    @Mapping(target = "synonymIds", ignore = true)
    @Mapping(target = "translations", source = "translations")
    List<WordsDTO> toDto(List<Words> words);

    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "translations", ignore = true)
    @Mapping(target = "synonyms", ignore = true)
    List<Words> fromDto(List<WordsDTO> dtos);
}