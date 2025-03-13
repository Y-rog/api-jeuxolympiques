package com.yrog.apijeuxolympiques.mapper;

import com.yrog.apijeuxolympiques.dto.event.EventDTO;
import com.yrog.apijeuxolympiques.pojo.Event;

public interface EventMapper{

    EventDTO toDTO (Event event);

    Event toEntity(EventDTO eventDTO);

}
