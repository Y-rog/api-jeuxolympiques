package com.yrog.apijeuxolympiques.pojo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @NotNull
    private String eventTitle;

    @NotNull
    private String eventDescription;

    @NotNull
    private String eventLocation;

    @NotNull
    private LocalDateTime eventDateTime;

    public Event() {

    }

    public Event(@NotNull String eventTitle, @NotNull String eventDescription, @NotNull String eventLocation, @NotNull LocalDateTime eventDateTime) {
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.eventLocation = eventLocation;
        this.eventDateTime = eventDateTime;
    }


}
