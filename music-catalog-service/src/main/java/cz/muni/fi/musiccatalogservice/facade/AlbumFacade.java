package cz.muni.fi.musiccatalogservice.facade;

import cz.muni.fi.musiccatalogservice.dto.AlbumDTO;
import cz.muni.fi.musiccatalogservice.dto.SongDTO;
import cz.muni.fi.musiccatalogservice.mapper.AlbumMapper;
import cz.muni.fi.musiccatalogservice.model.Album;
import cz.muni.fi.musiccatalogservice.service.AlbumService;
import cz.muni.fi.musiccatalogservice.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AlbumFacade {

    private final AlbumService albumService;
    private final SongService songService;
    private final AlbumMapper albumMapper;
    private final SongFacade songFacade;

    @Autowired
    public AlbumFacade(AlbumService albumService, SongService songService,
                       AlbumMapper albumMapper, SongFacade songFacade) {
        this.albumService = albumService;
        this.songService = songService;
        this.albumMapper = albumMapper;
        this.songFacade = songFacade;
    }

    public List<AlbumDTO> getAllAlbums() {
        return albumService.getAllAlbums().stream()
                .map(this::enrichAlbumWithSongs)
                .collect(Collectors.toList());
    }

    public List<AlbumDTO> getAlbumsByBand(Long bandId) {
        return albumService.getAlbumsByBand(bandId).stream()
                .map(this::enrichAlbumWithSongs)
                .collect(Collectors.toList());
    }

    public AlbumDTO getAlbumById(Long id) {
        Album album = albumService.getAlbumById(id);
        return enrichAlbumWithSongs(album);
    }

    public AlbumDTO createAlbum(AlbumDTO albumDTO) {
        Album album = albumMapper.toEntity(albumDTO);
        Album savedAlbum = albumService.createAlbum(album);
        return enrichAlbumWithSongs(savedAlbum);
    }

    public AlbumDTO updateAlbum(Long id, AlbumDTO albumDTO) {
        Album updatedAlbum = albumService.updateAlbum(id, albumMapper.toEntity(albumDTO));
        return enrichAlbumWithSongs(updatedAlbum);
    }

    public void deleteAlbum(Long id) {
        albumService.deleteAlbum(id);
    }

    private AlbumDTO enrichAlbumWithSongs(Album album) {
        AlbumDTO albumDTO = albumMapper.toDTO(album);
        List<SongDTO> songs = songFacade.getSongsByAlbum(album.getId());
        albumDTO.setSongs(songs);
        return albumDTO;
    }
}