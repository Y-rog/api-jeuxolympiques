package com.yrog.apijeuxolympiques.service.impl;

import com.yrog.apijeuxolympiques.dto.event.EventDTO;
import com.yrog.apijeuxolympiques.mapper.EventMapper;
import com.yrog.apijeuxolympiques.pojo.CartItem;
import com.yrog.apijeuxolympiques.pojo.Event;
import com.yrog.apijeuxolympiques.pojo.Offer;
import com.yrog.apijeuxolympiques.repository.CartRepository;
import com.yrog.apijeuxolympiques.repository.EventRepository;
import com.yrog.apijeuxolympiques.repository.OfferRepository;
import com.yrog.apijeuxolympiques.service.EventService;
import com.yrog.apijeuxolympiques.service.OfferService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yrog.apijeuxolympiques.pojo.Cart;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;


@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private CartRepository cartRepository;


    @Override
    public EventDTO createEvent(EventDTO eventDTO) {
        Event event = eventMapper.toEntity(eventDTO);
        Event savedEvent = eventRepository.save(event);
        return eventMapper.toDTO(savedEvent);
    }

    @Override
    public Optional<Event> getEventById(Long eventId) {
        return this.eventRepository.findById(eventId);
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

    @Override
    public boolean deleteEventById(Long id) {
        Optional<Event> event = eventRepository.findById(id);
        if (event.isPresent()) {
            eventRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public int getAvailablePlacesForEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        List<Cart> carts = cartRepository.findByItems_Offer_Event_EventId(eventId);

        int reservedPlaces = 0;

        for (Cart cart : carts) {
            for (CartItem cartItem : cart.getItems()) {
                if (cartItem.getOffer().getEvent().getEventId().equals(eventId)) {
                    int placesPerOffer = cartItem.getOffer().getOfferCategory().getPlacesPerOffer();
                    reservedPlaces += placesPerOffer;
                }
            }
        }

        return event.getEventPlacesNumber() - reservedPlaces;
    }


}
