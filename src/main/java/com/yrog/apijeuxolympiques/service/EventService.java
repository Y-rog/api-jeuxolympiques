package com.yrog.apijeuxolympiques.service;

import com.yrog.apijeuxolympiques.dto.event.EventDTO;
import com.yrog.apijeuxolympiques.pojo.Event;


public interface EventService {

    EventDTO createEvent(EventDTO eventDTO);

}