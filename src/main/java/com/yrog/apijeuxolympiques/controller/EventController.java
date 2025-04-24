package com.yrog.apijeuxolympiques.controller;

import com.yrog.apijeuxolympiques.dto.event.EventDTO;
import com.yrog.apijeuxolympiques.pojo.Event;
import com.yrog.apijeuxolympiques.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api-jeuxolympiques/event")
public class EventController {


    @Autowired
    private EventService eventService;

    @Secured("ROLE_ADMIN")
    @PostMapping
    public ResponseEntity<?> saveEvent(@Valid @RequestBody EventDTO eventDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                        .map(ObjectError::getDefaultMessage)
                        .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        EventDTO createdEvent = eventService.createEvent(eventDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);

    }

    @GetMapping
    public ResponseEntity<?> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateEventById(@PathVariable Long id, @Valid @RequestBody EventDTO eventDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        EventDTO updatedEvent = eventService.updateEventById(id, eventDTO); // Pass the ID to the service
        return ResponseEntity.status(HttpStatus.OK).body(updatedEvent); // Return the updated event with status 200 (OK)
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventById(@PathVariable Long id) {
        boolean deleted = eventService.deleteEventById(id); // Utilisation du service pour supprimer l'événement
        if (deleted) {
            return ResponseEntity.noContent().build(); // 204 No Content si suppression réussie
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found si événement introuvable
    }




    @GetMapping("/{eventId}/available-places")
    public int getAvailablePlaces(@PathVariable Long eventId) {
        return eventService.getAvailablePlacesForEvent(eventId);
    }

}
