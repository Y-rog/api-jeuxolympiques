package com.yrog.apijeuxolympiques;

import com.yrog.apijeuxolympiques.controller.EventController;
import com.yrog.apijeuxolympiques.dto.event.EventDTO;
import com.yrog.apijeuxolympiques.mapper.EventMapper;
import com.yrog.apijeuxolympiques.pojo.Event;
import com.yrog.apijeuxolympiques.repository.EventRepository;
import com.yrog.apijeuxolympiques.service.EventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class EventControllerUnitTest {

    @Mock
    private EventService eventService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private EventController eventController;

    @Test
    void testGetEventById_Success() {
        Event mockEvent = new Event();
        mockEvent.setEventId(1L);
        mockEvent.setEventTitle("Finale Football");
        mockEvent.setEventDescription("Match pour la médaille d'or");
        mockEvent.setEventLocation("Stade de France");
        mockEvent.setEventPlacesNumber(4);
        mockEvent.setEventDateTime(LocalDateTime.of(2024, 7, 30, 20, 0));

        when(eventService.getEventById(1L)).thenReturn(Optional.of(mockEvent));

        ResponseEntity<Event> response = eventController.getEventById(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(200 );
        Event responseEvent = response.getBody();
        if(responseEvent != null && responseEvent.getEventTitle() != null){
            assertThat(responseEvent.getEventTitle()).isEqualTo("Finale Football");
        } else {
            fail();
        }
    }

    @Test
    void testGetEventById_Failure() {
        when(eventService.getEventById(1000L)).thenReturn(Optional.empty());

        ResponseEntity<Event> response = eventController.getEventById(1000L);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void testGetEventById_NotFound() {
        Long nonExistentId = 999L;
        when(eventService.getEventById(nonExistentId)).thenReturn(Optional.empty());

        ResponseEntity<Event> response = eventController.getEventById(nonExistentId);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }


    @Test
    void testDeleteEventById_Success() {
        Long eventId = 1L;

        when(eventService.deleteEventById(eventId)).thenReturn(Boolean.TRUE);

        ResponseEntity<Void> response = eventController.deleteEventById(eventId);
        assertThat(response.getStatusCode().value()).isEqualTo(204);
    }

    @Test
    void testDeleteEventById_Failure() {
        Long eventId = 1000L;

        when(eventService.deleteEventById(eventId)).thenReturn(Boolean.FALSE);

        ResponseEntity<Void> response = eventController.deleteEventById(eventId);
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void testSaveEvent_Success() {
        EventDTO dto = new EventDTO();
        dto.setEventTitle("Finale de basketball");
        dto.setEventDescription("Match très attendu pour la médaille d’or");
        dto.setEventLocation("Stade de Lille");
        dto.setEventPlacesNumber(1000);
        dto.setEventDateTime(LocalDateTime.of(2024, 7, 22, 20, 0));

        when(bindingResult.hasErrors()).thenReturn(false);
        when(eventService.createEvent(dto)).thenReturn(dto);

        ResponseEntity<?> response = eventController.saveEvent(dto, bindingResult);

        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).isEqualTo(dto);

        verify(eventService, times(1)).createEvent(dto);
    }


    @Test
    void testSaveEvent_ValidationError() {
        EventDTO dto = new EventDTO();

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(
                Collections.singletonList(new ObjectError("field", "Erreur de validation")));

        ResponseEntity<?> response = eventController.saveEvent(dto, bindingResult);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        List<String> errors = (List<String>) response.getBody();
        assertThat(errors).contains("Erreur de validation");

        verify(eventService, never()).createEvent(any());
    }

    @Test
    void testGetAllEvents() {
        Event event = new Event();
        event.setEventId(1L);
        event.setEventTitle("Event Test");
        List<Event> eventList = List.of(event);

        when(eventService.getAllEvents()).thenReturn(eventList);

        ResponseEntity<?> response = eventController.getAllEvents();

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(eventList);
    }

    @Test
    void testUpdateEventById_Success() {
        Long id = 1L;
        EventDTO dto = new EventDTO();
        dto.setEventTitle("Épreuve d'athlétisme - 100m");
        dto.setEventDescription("Course reine des JO, à ne pas manquer");
        dto.setEventLocation("Stade de France");
        dto.setEventPlacesNumber(85000);
        dto.setEventDateTime(LocalDateTime.of(2024, 8, 1, 18, 30));

        when(bindingResult.hasErrors()).thenReturn(false);
        when(eventService.updateEventById(id, dto)).thenReturn(dto);

        ResponseEntity<?> response = eventController.updateEventById(id, dto, bindingResult);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(dto);
    }


    @Test
    void testUpdateEventById_ValidationError() {
        Long id = 1L;
        EventDTO dto = new EventDTO();

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(
                Collections.singletonList(new ObjectError("field", "Erreur update")));

        ResponseEntity<?> response = eventController.updateEventById(id, dto, bindingResult);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        List<String> errors = (List<String>) response.getBody();
        assertThat(errors).contains("Erreur update");

        verify(eventService, never()).updateEventById(anyLong(), any());
    }

    @Test
    void testGetAvailablePlaces() {
        Long eventId = 1L;
        int places = 42;

        when(eventService.getAvailablePlacesForEvent(eventId)).thenReturn(places);

        int result = eventController.getAvailablePlaces(eventId);

        assertThat(result).isEqualTo(places);
    }

}
