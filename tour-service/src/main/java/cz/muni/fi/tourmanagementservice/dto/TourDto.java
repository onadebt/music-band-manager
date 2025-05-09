package cz.muni.fi.tourmanagementservice.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class TourDto {
    private Long id;

    @NotNull(message = "Band ID cannot be null")
    private Long bandId;

    @NotBlank(message = "Tour name cannot be blank")
    private String tourName;

    private List<CityVisitDto> cityVisits;
}
