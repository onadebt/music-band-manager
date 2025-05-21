package cz.muni.fi.musiccatalogservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "album")
@EqualsAndHashCode(exclude = "album")
@Schema(description = "Represents a song that may be part of an album")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the song", example = "1")
    private Long id;

    @NotBlank(message = "Song name cannot be blank")
    @Size(min = 1, max = 255, message = "Song name must be between 1 and 255 characters")
    @Schema(description = "The name of the song", example = "Money")
    private String name;

    @Min(value = 1, message = "Song duration must be at least 1 second")
    @Schema(description = "Duration of the song in seconds", example = "382")
    private int duration;

    @NotNull(message = "Band ID cannot be null")
    @Schema(description = "ID of the band that performed this song", example = "42")
    private Long bandId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "album_id")
    @Schema(description = "The album this song belongs to", nullable = true)
    private Album album;
}