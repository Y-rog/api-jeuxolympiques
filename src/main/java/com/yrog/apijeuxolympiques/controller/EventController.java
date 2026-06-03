package com.yrog.apijeuxolympiques.controller;

import com.yrog.apijeuxolympiques.dto.event.EventDTO;
import com.yrog.apijeuxolympiques.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller gérant les opérations sur les événements olympiques.
 */
@RestController
@RequestMapping("api-jeuxolympiques/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    /**
     * Crée un nouvel événement.
     */
    @PostMapping
    public ResponseEntity<EventDTO> saveEvent(@Valid @RequestBody EventDTO eventDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eventService.createEvent(eventDTO));
    }

    /**
     * Récupère tous les événements.
     */
    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    /**
     * Récupère un événement par son identifiant.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    /**
     * Met à jour un événement existant.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> updateEventById(@PathVariable Long id,
                                                    @Valid @RequestBody EventDTO eventDTO) {
        return ResponseEntity.ok(eventService.updateEventById(id, eventDTO));
    }

    /**
     * Supprime un événement par son identifiant.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventById(@PathVariable Long id) {
        eventService.deleteEventById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retourne le nombre de places disponibles pour un événement.
     */
    @GetMapping("/{eventId}/available-places")
    public ResponseEntity<Integer> getAvailablePlaces(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getAvailablePlacesForEvent(eventId));
    }
}
