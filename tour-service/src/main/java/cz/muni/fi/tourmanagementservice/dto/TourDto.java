package cz.muni.fi.tourmanagementservice.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TourDto {
    private Long id;

    @NotNull(message = "Band ID cannot be null")
    private Long bandId;

    @NotBlank(message = "Tour name cannot be blank")
    private String tourName;

    @Valid
    private List<CityVisitDto> cityVisits;
}
