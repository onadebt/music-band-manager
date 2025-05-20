package cz.muni.fi.bandmanagementservice.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author Tomáš MAREK
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class BandInfoUpdate {
    private Long id;

    private String name;

    private String musicalStyle;

    private Long managerId;

    private String logoUrl;
}
