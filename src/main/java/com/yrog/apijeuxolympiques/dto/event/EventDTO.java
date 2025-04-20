package com.yrog.apijeuxolympiques.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
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

    @NotNull(message = "Le nombre de places est obligatoire")
    @Schema(description = "Nombre de places", example = "1000")
    @NotNull
    @Min(value = 1, message ="Le nombre doit être supérieur à 1")
    @Max(value = 999999, message = "Le nombre doit être inferieur à 999999")
    private Integer eventPlacesNumber;

    @NotNull(message = "La date et l'heur sont obligatoire")
    @Schema(description = "Titre de l'événement", example = "22/07/2024 20:00")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime eventDateTime;

    public EventDTO() {}

}
