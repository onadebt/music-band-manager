package cz.muni.fi.musiccatalogservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SongDto {
    private Long id;

    @NotBlank(message = "Song name cannot be blank")
    private String name;

    @Min(value = 1, message = "Song duration must be at least 1 second")
    private int duration;

    @NotNull(message = "Band ID cannot be null")
    private Long bandId;

    private Long albumId;
}