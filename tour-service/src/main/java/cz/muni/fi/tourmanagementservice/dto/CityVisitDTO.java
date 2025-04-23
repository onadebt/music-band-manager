package cz.muni.fi.tourmanagementservice.dto;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class CityVisitDTO {
    private Long id;

    @NotNull(message = "Tour ID cannot be null")
    private Long tourId;

    @NotBlank(message = "City name cannot be blank")
    private String cityName;

    @NotNull(message = "Date From cannot be null")
    @FutureOrPresent(message = "Date From must be in the present or future")
    private Date dateFrom;

    @NotNull(message = "Date From cannot be null")
    @FutureOrPresent(message = "Date From must be in the present or future")
    private Date dateTo;
}
