package com.yrog.apijeuxolympiques.service;

import com.yrog.apijeuxolympiques.dto.event.EventDTO;
import java.util.List;

/**
 * Service gérant les opérations sur les événements olympiques.
 */
public interface EventService {

    /**
     * Crée un nouvel événement.
     */
    EventDTO createEvent(EventDTO eventDTO);

    /**
     * Retourne un événement par son identifiant.
     */
    EventDTO getEventById(Long id);

    /**
     * Retourne tous les événements.
     */
    List<EventDTO> getAllEvents();

    /**
     * Met à jour un événement existant.
     */
    EventDTO updateEventById(Long id, EventDTO eventDTO);

    /**
     * Supprime un événement par son identifiant.
     */
    void deleteEventById(Long id);

    /**
     * Retourne le nombre de places disponibles pour un événement.
     */
    int getAvailablePlacesForEvent(Long eventId);
}