package cz.muni.fi.tourmanagementservice.dto;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CityVisitDto {
    private Long id;

    @NotBlank(message = "City name cannot be blank")
    private String cityName;

    @NotNull(message = "Date From cannot be null")
    @FutureOrPresent(message = "Date From must be in the present or future")
    private Date dateFrom;

    @NotNull(message = "Date To cannot be null")
    @FutureOrPresent(message = "Date To must be in the present or future")
    private Date dateTo;
}
