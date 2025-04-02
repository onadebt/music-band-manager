package cz.muni.fi.bandmanagementservice.band.data.model;

/**
 * @author Tomáš MAREK
 */
public record BandInfoUpdate(Long id, String name, String musicalStyle, Long managerId, String logoUrl) {
}
