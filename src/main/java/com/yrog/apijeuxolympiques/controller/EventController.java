package com.yrog.apijeuxolympiques.controller;

import com.yrog.apijeuxolympiques.pojo.Event;
import com.yrog.apijeuxolympiques.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api-jeuxolympiques/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    public void createEvent(@RequestBody Event event) {

        this.eventService.createEvent(event);

    }


}
