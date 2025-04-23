package com.yrog.apijeuxolympiques.service;

import com.yrog.apijeuxolympiques.dto.event.EventDTO;
import com.yrog.apijeuxolympiques.pojo.Event;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


public interface EventService {

    EventDTO createEvent(EventDTO eventDTO);

    EventDTO updateEventById(@PathVariable Long id, EventDTO eventDTO);

    Event getEventById(Long eventId);

    List<Event> getAllEvents();

    boolean deleteEventById(Long id);

    int getAvailablePlacesForEvent(Long eventId);
}