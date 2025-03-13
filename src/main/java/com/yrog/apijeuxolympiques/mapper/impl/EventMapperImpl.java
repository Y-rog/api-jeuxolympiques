package com.yrog.apijeuxolympiques.mapper.impl;

import com.yrog.apijeuxolympiques.dto.event.EventDTO;
import com.yrog.apijeuxolympiques.mapper.EventMapper;
import com.yrog.apijeuxolympiques.pojo.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapperImpl implements EventMapper {

    @Override
    public EventDTO toDTO (Event event) {

        if (event == null) return null;

        EventDTO eventDTO = new EventDTO();
        eventDTO.setEventTitle(event.getEventTitle());
        eventDTO.setEventDescription(event.getEventDescription());
        eventDTO.setEventLocation(event.getEventLocation());
        eventDTO.setEventDateTime(event.getEventDateTime());

        return eventDTO;

    }

    @Override
    public Event toEntity(EventDTO eventDTO) {

        if (eventDTO == null) return null;

        Event event = new Event();
        event.setEventTitle(eventDTO.getEventTitle());
        event.setEventDescription(eventDTO.getEventDescription());
        event.setEventLocation(eventDTO.getEventLocation());
        event.setEventDateTime(eventDTO.getEventDateTime());

        return event;
    }

}
