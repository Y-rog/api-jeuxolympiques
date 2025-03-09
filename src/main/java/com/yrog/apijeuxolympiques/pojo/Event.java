package com.yrog.apijeuxolympiques.pojo;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperties;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long eventId;

    private String eventTitle;
    private String eventDescription;
    private String eventLocation;
    private LocalDateTime eventDateTime;

    public Event() {

    }

    public Event(String eventTitle, String eventDescription, String eventLocation, LocalDateTime eventDateTime) {
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.eventLocation = eventLocation;
        this.eventDateTime = eventDateTime;
    }


}
