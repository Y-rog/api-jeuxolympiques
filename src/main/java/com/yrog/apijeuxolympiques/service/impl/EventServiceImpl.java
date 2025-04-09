package com.yrog.apijeuxolympiques.service.impl;

import com.yrog.apijeuxolympiques.dto.event.EventDTO;
import com.yrog.apijeuxolympiques.mapper.EventMapper;
import com.yrog.apijeuxolympiques.pojo.Event;
import com.yrog.apijeuxolympiques.repository.EventRepository;
import com.yrog.apijeuxolympiques.service.EventService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapper eventMapper;

    @Override
    public EventDTO createEvent(EventDTO eventDTO) {
        Event event = eventMapper.toEntity(eventDTO);
        Event savedEvent = eventRepository.save(event);
        return eventMapper.toDTO(savedEvent);
    }

    @Override
    public Event getEventById(Long eventId) {
        return this.eventRepository.findById(eventId).orElse(null);
    }

    @Override
    public List<Event> getAllEvents() {
        return this.eventRepository.findAll();
    }

    @Override
    public EventDTO updateEventById(Long id, EventDTO eventDTO) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + id));

        existingEvent.setEventTitle(eventDTO.getEventTitle());
        existingEvent.setEventDescription(eventDTO.getEventDescription());
        existingEvent.setEventLocation(eventDTO.getEventLocation());
        existingEvent.setEventPlacesNumber(eventDTO.getEventPlacesNumber());
        existingEvent.setEventDateTime(eventDTO.getEventDateTime());

        Event updatedEvent = eventRepository.save(existingEvent);

        return eventMapper.toDTO(updatedEvent);
    }

}
