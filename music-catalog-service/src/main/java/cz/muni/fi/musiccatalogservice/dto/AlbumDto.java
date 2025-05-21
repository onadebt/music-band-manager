package cz.muni.fi.musiccatalogservice.dto;

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
public class AlbumDto {
    private Long id;

    @NotBlank(message = "Album title cannot be blank")
    private String title;

    @NotNull(message = "Release date cannot be null")
    @PastOrPresent(message = "Release date must be in the past or present")
    private LocalDateTime releaseDate;

    @NotNull(message = "Band ID cannot be null")
    private Long bandId;

    private List<SongDto> songs;
}