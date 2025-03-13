package com.yrog.apijeuxolympiques.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "Informations sur l'événement'")
public class EventDTO {

    @NotNull(message = "Le titre est obligatoire")
    @Schema(description = "Titre de l'événement", example = "Finale de basketball")
    @Size(min = 1, max = 50, message = "Le titre doit avoir entre 1 et 50 caractères")
    private String eventTitle;

    @NotNull(message = "La description est obligatoire")
    @Schema(description = "Description de l'événement", example = "Venez voir la grande finale du tournoi")
    @Size(min = 1, max=500, message ="La description doit avoir entre 1 et 500 caractères")
    private String eventDescription;

    @NotNull(message = "La location est obligatoire")
    @Schema(description = "Lieu de l'événement", example = "Stade de Lille")
    @Size(min = 1, max=50, message ="Le lieu doit avoir entre 1 et 50 caractères")
    private String eventLocation;

    @NotNull(message = "La date et l'heur sont obligatoire")
    @Schema(description = "Titre de l'événement", example = "22/07/2024 20:00")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime eventDateTime;

    public EventDTO() {}

    public EventDTO(String eventTitle, String eventDescription, String eventLocation, LocalDateTime eventDateTime) {
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.eventLocation = eventLocation;
        this.eventDateTime = eventDateTime;
    }

}
