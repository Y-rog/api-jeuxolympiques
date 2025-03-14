package com.yrog.apijeuxolympiques.service;

import com.yrog.apijeuxolympiques.dto.event.EventDTO;
import com.yrog.apijeuxolympiques.pojo.Event;

import java.util.List;


public interface EventService {

    EventDTO createEvent(EventDTO eventDTO);

    Event getEventById(Long eventId);

    List<Event> getAllEvents();

}