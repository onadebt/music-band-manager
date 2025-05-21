package cz.muni.fi.musiccatalogservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "Data Transfer Object for an album")
public class AlbumDto {
    @Schema(description = "Unique identifier of the album", example = "1")
    private Long id;

    @NotBlank(message = "Album title cannot be blank")
    @Schema(description = "The title of the album", example = "Dark Side of the Moon")
    private String title;

    @NotNull(message = "Release date cannot be null")
    @PastOrPresent(message = "Release date must be in the past or present")
    @Schema(description = "The release date of the album", example = "1973-03-01T00:00:00")
    private LocalDateTime releaseDate;

    @NotNull(message = "Band ID cannot be null")
    @Schema(description = "ID of the band that created this album", example = "42")
    private Long bandId;

    @Schema(description = "List of songs on this album")
    private List<SongDto> songs;
}