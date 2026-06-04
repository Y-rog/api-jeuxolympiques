package com.yrog.apijeuxolympiques;

import com.yrog.apijeuxolympiques.controller.EventController;
import com.yrog.apijeuxolympiques.dto.event.EventDTO;
import com.yrog.apijeuxolympiques.service.EventService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class EventControllerUnitTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    private EventDTO buildEventDTO() {
        return new EventDTO(
                null,
                "Finale Football",
                "Match pour la médaille d'or",
                "Stade de France",
                4,
                LocalDateTime.of(2024, 7, 30, 20, 0)
        );
    }

    @Test
    void testGetEventById_Success() {
        EventDTO mockEvent = buildEventDTO();
        when(eventService.getEventById(1L)).thenReturn(mockEvent);

        ResponseEntity<EventDTO> response = eventController.getEventById(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().eventTitle()).isEqualTo("Finale Football");
    }

    @Test
    void testGetEventById_NotFound() {
        when(eventService.getEventById(999L))
                .thenThrow(new EntityNotFoundException("Événement introuvable : 999"));

        assertThatThrownBy(() -> eventController.getEventById(999L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void testDeleteEventById_Success() {
        doNothing().when(eventService).deleteEventById(1L);

        ResponseEntity<Void> response = eventController.deleteEventById(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(204);
        verify(eventService, times(1)).deleteEventById(1L);
    }

    @Test
    void testDeleteEventById_NotFound() {
        doThrow(new EntityNotFoundException("Événement introuvable : 1000"))
                .when(eventService).deleteEventById(1000L);

        assertThatThrownBy(() -> eventController.deleteEventById(1000L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void testSaveEvent_Success() {
        EventDTO dto = buildEventDTO();
        when(eventService.createEvent(dto)).thenReturn(dto);

        ResponseEntity<EventDTO> response = eventController.saveEvent(dto);

        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).isEqualTo(dto);
        verify(eventService, times(1)).createEvent(dto);
    }

    @Test
    void testGetAllEvents() {
        EventDTO event = buildEventDTO();
        List<EventDTO> eventList = List.of(event);
        when(eventService.getAllEvents()).thenReturn(eventList);

        ResponseEntity<List<EventDTO>> response = eventController.getAllEvents();

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(eventList);
    }

    @Test
    void testUpdateEventById_Success() {
        EventDTO dto = buildEventDTO();
        when(eventService.updateEventById(1L, dto)).thenReturn(dto);

        ResponseEntity<EventDTO> response = eventController.updateEventById(1L, dto);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(dto);
    }

    @Test
    void testGetAvailablePlaces() {
        when(eventService.getAvailablePlacesForEvent(1L)).thenReturn(42);

        ResponseEntity<Integer> response = eventController.getAvailablePlaces(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(42);
    }
}
