package cz.muni.fi.musiccatalogservice.service;

import cz.muni.fi.musiccatalogservice.exception.ResourceNotFoundException;
import cz.muni.fi.musiccatalogservice.model.Album;
import cz.muni.fi.musiccatalogservice.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;

    @Autowired
    public AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    public List<Album> getAllAlbums() {
        return albumRepository.findAll();
    }

    public List<Album> getAlbumsByBand(Long bandId) {
        if (bandId == null) {
            throw new IllegalArgumentException("Band ID cannot be null");
        }

        if (bandId < 0) {
            throw new IllegalArgumentException("Invalid band ID: " + bandId);
        }

        return albumRepository.findByBandId(bandId);
    }

    public Album getAlbumById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Album ID cannot be null");
        }

        if (id < 0) {
            throw new IllegalArgumentException("Invalid album ID: " + id);
        }

        return albumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Album not found with id: " + id));
    }

    @Transactional
    public Album createAlbum(Album album) {
        if (album == null) {
            throw new IllegalArgumentException("Album cannot be null");
        }

        return albumRepository.save(album);
    }

    @Transactional
    public Album updateAlbum(Long id, Album albumDetails) {
        if (id == null) {
            throw new IllegalArgumentException("Album ID cannot be null");
        }

        if (albumDetails == null) {
            throw new IllegalArgumentException("Album details cannot be null");
        }

        Album album = getAlbumById(id);
        album.setTitle(albumDetails.getTitle());
        album.setReleaseDate(albumDetails.getReleaseDate());
        album.setBandId(albumDetails.getBandId());
        return albumRepository.save(album);
    }

    @Transactional
    public void deleteAlbum(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Album ID cannot be null");
        }

        if (id < 0) {
            throw new IllegalArgumentException("Invalid album ID: " + id);
        }

        // Check if album exists
        getAlbumById(id);
        albumRepository.deleteById(id);
    }
}