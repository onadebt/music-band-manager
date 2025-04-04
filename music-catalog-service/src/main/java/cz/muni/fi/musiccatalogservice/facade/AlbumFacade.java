package cz.muni.fi.musiccatalogservice.facade;

import cz.muni.fi.musiccatalogservice.dto.AlbumDTO;
import cz.muni.fi.musiccatalogservice.exception.ResourceNotFoundException;
import cz.muni.fi.musiccatalogservice.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlbumFacade {

    private final AlbumService albumService;

    @Autowired
    public AlbumFacade(AlbumService albumService) {
        this.albumService = albumService;
    }

    public List<AlbumDTO> getAllAlbums() {
        return albumService.getAllAlbums();
    }

    public List<AlbumDTO> getAlbumsByBand(Long bandId) {
        return albumService.getAlbumsByBand(bandId);
    }

    public AlbumDTO getAlbumById(Long id) {
        return albumService.getAlbumById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Album not found with id: " + id));
    }

    public AlbumDTO createAlbum(AlbumDTO albumDTO) {
        return albumService.createAlbum(albumDTO);
    }

    public AlbumDTO updateAlbum(Long id, AlbumDTO albumDTO) {
        AlbumDTO updatedAlbum = albumService.updateAlbum(id, albumDTO);
        if (updatedAlbum == null) {
            throw new ResourceNotFoundException("Album not found with id: " + id);
        }
        return updatedAlbum;
    }

    public void deleteAlbum(Long id) {
        boolean deleted = albumService.deleteAlbum(id);
        if (!deleted) {
            throw new ResourceNotFoundException("Album not found with id: " + id);
        }
    }
}