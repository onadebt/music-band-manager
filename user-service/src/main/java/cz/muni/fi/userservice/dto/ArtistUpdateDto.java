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
public class ArtistUpdateDto extends UserUpdateDto {
    private String stageName;
    private String bio;
    private String skills;
    private Set<Long> bandIds;
}
