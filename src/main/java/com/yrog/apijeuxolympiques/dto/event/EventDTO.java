package com.yrog.apijeuxolympiques.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * DTO représentant les informations d'un événement olympique.
 */
@Getter
@Setter
@Schema(description = "Informations sur l'événement")
public class EventDTO {

        private Long eventId;

        @NotNull(message = "Le titre est obligatoire")
        @Size(min = 1, max = 50)
        private String eventTitle;

        @NotNull(message = "La description est obligatoire")
        @Size(min = 1, max = 500)
        private String eventDescription;

        @NotNull(message = "La location est obligatoire")
        @Size(min = 1, max = 50)
        private String eventLocation;

        @NotNull(message = "Le nombre de places est obligatoire")
        @Min(1) @Max(999999)
        private Integer eventPlacesNumber;

        @NotNull(message = "La date et l'heure sont obligatoires")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime eventDateTime;
}