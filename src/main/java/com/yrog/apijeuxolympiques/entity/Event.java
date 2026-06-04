package com.yrog.apijeuxolympiques.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entité représentant un événement sportif des Jeux Olympiques.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;

    @Column(nullable = false, length = 200)
    private String eventTitle;

    @Column(nullable = false, length = 500)
    private String eventDescription;

    @Column(nullable = false, length = 200)
    private String eventLocation;

    /**
     * Nombre de places disponibles pour cet événement.
     */
    @Column(nullable = false)
    private Integer eventPlacesNumber;

    /**
     * Date et heure de l'événement.
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime eventDateTime;
}
