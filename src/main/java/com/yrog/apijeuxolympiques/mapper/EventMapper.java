package com.yrog.apijeuxolympiques.mapper;

import com.yrog.apijeuxolympiques.dto.event.EventDTO;
import com.yrog.apijeuxolympiques.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper MapStruct pour convertir entre Event et EventDTO.
 */
@Mapper(componentModel = "spring")
public interface EventMapper {

    /**
     * Convertit un Event en EventDTO.
     */
    @Mapping(source = "eventId", target = "eventId")
    EventDTO toDTO(Event event);

    /**
     * Convertit un EventDTO en Event.
     */
    @Mapping(target = "eventId", ignore = true)@Mapping(target = "eventId", ignore = true)
    Event toEntity(EventDTO eventDTO);
}