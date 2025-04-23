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
        return albumRepository.findByBandId(bandId);
    }

    public Album getAlbumById(Long id) {
        return albumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Album not found with id: " + id));
    }

    @Transactional
    public Album createAlbum(Album album) {
        return albumRepository.save(album);
    }

    @Transactional
    public Album updateAlbum(Long id, Album albumDetails) {
        Album album = getAlbumById(id);
        album.setTitle(albumDetails.getTitle());
        album.setReleaseDate(albumDetails.getReleaseDate());
        album.setBandId(albumDetails.getBandId());
        return albumRepository.save(album);
    }

    @Transactional
    public void deleteAlbum(Long id) {
        getAlbumById(id);
        albumRepository.deleteById(id);
    }
}