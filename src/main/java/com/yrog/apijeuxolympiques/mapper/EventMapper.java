package com.yrog.apijeuxolympiques.mapper;

import com.yrog.apijeuxolympiques.dto.event.EventDTO;
import com.yrog.apijeuxolympiques.entity.Event;
import org.mapstruct.Mapper;

/**
 * Mapper MapStruct pour convertir entre Event et EventDTO.
 */
@Mapper(componentModel = "spring")
public interface EventMapper {

    /**
     * Convertit un Event en EventDTO.
     */
    EventDTO toDTO(Event event);

    /**
     * Convertit un EventDTO en Event.
     */
    Event toEntity(EventDTO eventDTO);
}