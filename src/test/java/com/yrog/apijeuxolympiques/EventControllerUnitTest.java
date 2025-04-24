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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class EventControllerUnitTest {

    @Mock
    private EventService eventService;

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
        ResponseEntity<Event> response = eventController.getEventById(1000L);
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void testDeleteEventById_Success() {
        Long eventId = 1L;

        when(eventService.deleteEventById(eventId)).thenReturn(Boolean.TRUE);

        ResponseEntity<Void> response = eventController.deleteEventById(eventId);
        assertThat(response.getStatusCode().value()).isEqualTo(204);
    }

}
