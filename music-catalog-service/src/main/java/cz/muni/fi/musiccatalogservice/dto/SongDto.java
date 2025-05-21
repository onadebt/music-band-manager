package cz.muni.fi.musiccatalogservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "Data Transfer Object for a song")
public class SongDto {
    @Schema(description = "Unique identifier of the song", example = "1")
    private Long id;

    @NotBlank(message = "Song name cannot be blank")
    @Schema(description = "The name of the song", example = "Money")
    private String name;

    @Min(value = 1, message = "Song duration must be at least 1 second")
    @Schema(description = "Duration of the song in seconds", example = "382")
    private int duration;

    @NotNull(message = "Band ID cannot be null")
    @Schema(description = "ID of the band that performed this song", example = "42")
    private Long bandId;

    @Schema(description = "ID of the album this song belongs to", example = "1", nullable = true)
    private Long albumId;
}