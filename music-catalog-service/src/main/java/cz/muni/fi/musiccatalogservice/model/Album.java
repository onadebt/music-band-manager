package cz.muni.fi.musiccatalogservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "songs")
@EqualsAndHashCode(exclude = "songs")
@Schema(description = "Represents a music album with associated songs")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the album", example = "1")
    private Long id;

    @NotBlank(message = "Album title cannot be blank")
    @Size(min = 1, max = 255, message = "Album title must be between 1 and 255 characters")
    @Schema(description = "The title of the album", example = "Dark Side of the Moon")
    private String title;

    @NotNull(message = "Release date cannot be null")
    @PastOrPresent(message = "Release date must be in the past or present")
    @Schema(description = "The release date of the album", example = "1973-03-01T00:00:00")
    private LocalDateTime releaseDate;

    @NotNull(message = "Band ID cannot be null")
    @Schema(description = "ID of the band that created this album", example = "42")
    private Long bandId;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Schema(description = "List of songs on this album")
    private List<Song> songs = new ArrayList<>();

    public void addSong(Song song) {
        songs.add(song);
        song.setAlbum(this);
    }

    public void removeSong(Song song) {
        songs.remove(song);
        song.setAlbum(null);
    }
}