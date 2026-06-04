package com.yrog.apijeuxolympiques.service.impl;

import com.yrog.apijeuxolympiques.dto.event.EventDTO;
import com.yrog.apijeuxolympiques.entity.Cart;
import com.yrog.apijeuxolympiques.entity.CartItem;
import com.yrog.apijeuxolympiques.entity.Event;
import com.yrog.apijeuxolympiques.mapper.EventMapper;
import com.yrog.apijeuxolympiques.repository.CartRepository;
import com.yrog.apijeuxolympiques.repository.EventRepository;
import com.yrog.apijeuxolympiques.service.EventService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implémentation du service gérant les événements olympiques.
 */
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CartRepository cartRepository;

    @Override
    public EventDTO createEvent(EventDTO eventDTO) {
        return eventMapper.toDTO(eventRepository.save(eventMapper.toEntity(eventDTO)));
    }

    @Override
    public EventDTO getEventById(Long id) {
        return eventMapper.toDTO(
                eventRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Événement introuvable : " + id))
        );
    }

    @Override
    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(eventMapper::toDTO)
                .toList();
    }

    @Override
    public EventDTO updateEventById(Long id, EventDTO eventDTO) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Événement introuvable : " + id));

        existingEvent.setEventTitle(eventDTO.getEventTitle());
        existingEvent.setEventDescription(eventDTO.getEventDescription());
        existingEvent.setEventLocation(eventDTO.getEventLocation());
        existingEvent.setEventPlacesNumber(eventDTO.getEventPlacesNumber());
        existingEvent.setEventDateTime(eventDTO.getEventDateTime());

        return eventMapper.toDTO(eventRepository.save(existingEvent));
    }

    @Override
    public void deleteEventById(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new EntityNotFoundException("Événement introuvable : " + id);
        }
        eventRepository.deleteById(id);
    }

    @Override
    public int getAvailablePlacesForEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Événement introuvable : " + eventId));

        int reservedPlaces = cartRepository.findByItems_Offer_Event_EventId(eventId)
                .stream()
                .flatMap(cart -> cart.getItems().stream())
                .filter(item -> item.getOffer().getEvent().getEventId().equals(eventId))
                .mapToInt(item -> item.getOffer().getOfferCategory().getPlacesPerOffer())
                .sum();

        return event.getEventPlacesNumber() - reservedPlaces;
    }
}
