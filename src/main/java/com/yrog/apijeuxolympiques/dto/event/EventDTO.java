package com.yrog.apijeuxolympiques.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO représentant les informations d'un événement olympique.
 *
 * @param eventTitle       le titre de l'événement
 * @param eventDescription la description de l'événement
 * @param eventLocation    le lieu de l'événement
 * @param eventPlacesNumber le nombre de places disponibles
 * @param eventDateTime    la date et heure de l'événement
 */
@Schema(description = "Informations sur l'événement'")
public record EventDTO (

    @NotNull(message = "Le titre est obligatoire")
    @Schema(description = "Titre de l'événement", example = "Finale de basketball")
    @Size(min = 1, max = 50, message = "Le titre doit avoir entre 1 et 50 caractères")
    String eventTitle,

    @NotNull(message = "La description est obligatoire")
    @Schema(description = "Description de l'événement", example = "Venez voir la grande finale du tournoi")
    @Size(min = 1, max=500, message ="La description doit avoir entre 1 et 500 caractères")
    String eventDescription,

    @NotNull(message = "La location est obligatoire")
    @Schema(description = "Lieu de l'événement", example = "Stade de Lille")
    @Size(min = 1, max=50, message ="Le lieu doit avoir entre 1 et 50 caractères")
    String eventLocation,

    @NotNull(message = "Le nombre de places est obligatoire")
    @Schema(description = "Nombre de places", example = "1000")
    @Min(value = 1, message ="Le nombre doit être supérieur à 1")
    @Max(value = 999999, message = "Le nombre doit être inferieur à 999999")
    Integer eventPlacesNumber,

    @NotNull(message = "La date et l'heur sont obligatoire")
    @Schema(description = "Date et heure de l'événement", example = "22/07/2024 20:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime eventDateTime

) {}
