package cz.muni.fi.userservice.facade.interfaces;

import cz.muni.fi.userservice.dto.ArtistDto;
import cz.muni.fi.userservice.dto.ArtistUpdateDto;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

public interface ArtistFacade {
    ArtistDto register(@NotNull ArtistDto artistDTO);

    ArtistDto findById(@NotNull Long id);

    ArtistDto findByUsername(@NotNull String username);

    List<ArtistDto> findAll();

    List<ArtistDto> findByBandIds(@NotNull Set<Long> bandIds);

    void deleteById(@NotNull Long id);

    ArtistDto updateBandIds(@NotNull Long artistId, @NotNull Set<Long> bandsIds);

    ArtistDto update(@NotNull Long id, @NotNull ArtistUpdateDto artistUpdateDto);

    ArtistDto linkArtistToBand(@NotNull Long bandId, @NotNull Long artistId);

    ArtistDto unlinkArtistFromBand(@NotNull Long bandId, @NotNull Long artistId);
}
