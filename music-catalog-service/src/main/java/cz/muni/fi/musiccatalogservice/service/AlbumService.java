package cz.muni.fi.musiccatalogservice.service;

import cz.muni.fi.musiccatalogservice.dto.AlbumDTO;
import cz.muni.fi.musiccatalogservice.model.Album;
import cz.muni.fi.musiccatalogservice.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final SongService songService;

    @Autowired
    public AlbumService(AlbumRepository albumRepository, @Lazy SongService songService) {
        this.albumRepository = albumRepository;
        this.songService = songService;
    }

    public List<AlbumDTO> getAllAlbums() {
        return albumRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AlbumDTO> getAlbumsByBand(Long bandId) {
        return albumRepository.findByBandId(bandId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<AlbumDTO> getAlbumById(Long id) {
        return albumRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Transactional
    public AlbumDTO createAlbum(AlbumDTO albumDTO) {
        Album album = convertToEntity(albumDTO);
        Album savedAlbum = albumRepository.save(album);
        return convertToDTO(savedAlbum);
    }

    @Transactional
    public AlbumDTO updateAlbum(Long id, AlbumDTO albumDTO) {
        Optional<Album> existingAlbum = albumRepository.findById(id);

        if (existingAlbum.isPresent()) {
            Album album = existingAlbum.get();
            album.setTitle(albumDTO.getTitle());
            album.setReleaseDate(albumDTO.getReleaseDate());
            album.setBandId(albumDTO.getBandId());

            Album updatedAlbum = albumRepository.save(album);
            return convertToDTO(updatedAlbum);
        }

        return null;
    }

    @Transactional
    public boolean deleteAlbum(Long id) {
        if (albumRepository.existsById(id)) {
            albumRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Fix for circular dependency
    public AlbumDTO convertToDTO(Album album) {
        AlbumDTO albumDTO = new AlbumDTO();
        albumDTO.setId(album.getId());
        albumDTO.setTitle(album.getTitle());
        albumDTO.setReleaseDate(album.getReleaseDate());
        albumDTO.setBandId(album.getBandId());

        // Only fetch songs if SongService is available
        if (songService != null) {
            albumDTO.setSongs(songService.getSongsByAlbum(album.getId()));
        } else {
            albumDTO.setSongs(new ArrayList<>());
        }

        return albumDTO;
    }

    // Method for SongService to use to avoid circular dependency
    public AlbumDTO convertToDTOWithoutSongs(Album album) {
        AlbumDTO albumDTO = new AlbumDTO();
        albumDTO.setId(album.getId());
        albumDTO.setTitle(album.getTitle());
        albumDTO.setReleaseDate(album.getReleaseDate());
        albumDTO.setBandId(album.getBandId());
        albumDTO.setSongs(new ArrayList<>());
        return albumDTO;
    }

    private Album convertToEntity(AlbumDTO albumDTO) {
        Album album = new Album();
        album.setTitle(albumDTO.getTitle());
        album.setReleaseDate(albumDTO.getReleaseDate());
        album.setBandId(albumDTO.getBandId());
        return album;
    }

    // Method to get Album entity by ID for use in other services
    public Optional<Album> getAlbumEntityById(Long id) {
        return albumRepository.findById(id);
    }
}