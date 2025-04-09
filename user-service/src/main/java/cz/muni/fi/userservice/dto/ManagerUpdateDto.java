package cz.muni.fi.userservice.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ManagerUpdateDto extends UserUpdateDto {
    private String companyName;
    private Set<Long> managedBandIds;
}
