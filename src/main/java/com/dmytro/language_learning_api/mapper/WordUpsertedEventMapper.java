package com.dmytro.language_learning_api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dmytro.language_learning_api.kafka.producer.word.WordUpsertedEvent;
import com.dmytro.language_learning_api.model.Translation;
import com.dmytro.language_learning_api.model.Words;

@Mapper(componentModel = "spring")
public interface WordUpsertedEventMapper {

    @Mapping(target = "wordId", source = "id")
    @Mapping(target = "userEmail", source = "owner.email")
    WordUpsertedEvent toEvent(Words word);

    WordUpsertedEvent.TranslationPayload toTranslationPayload(Translation translation);

    List<WordUpsertedEvent.TranslationPayload> toTranslationPayload(List<Translation> translations);
}