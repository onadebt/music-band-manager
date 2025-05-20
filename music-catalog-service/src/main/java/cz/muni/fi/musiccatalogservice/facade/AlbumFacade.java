package cz.muni.fi.musiccatalogservice.facade;

import cz.muni.fi.musiccatalogservice.dto.AlbumDto;
import cz.muni.fi.musiccatalogservice.dto.SongDto;
import cz.muni.fi.musiccatalogservice.mapper.AlbumMapper;
import cz.muni.fi.musiccatalogservice.model.Album;
import cz.muni.fi.musiccatalogservice.service.AlbumService;
import cz.muni.fi.musiccatalogservice.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
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

    public List<AlbumDto> getAllAlbums() {
        List<Album> albums = albumService.getAllAlbums();
        return albums.stream()
                .map(this::enrichAlbumWithSongs)
                .collect(Collectors.toList());
    }

    public List<AlbumDto> getAlbumsByBand(Long bandId) {
        if (bandId == null) {
            throw new IllegalArgumentException("Band ID cannot be null");
        }

        List<Album> albums = albumService.getAlbumsByBand(bandId);
        return albums.stream()
                .map(this::enrichAlbumWithSongs)
                .collect(Collectors.toList());
    }

    public AlbumDto getAlbumById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Album ID cannot be null");
        }

        Album album = albumService.getAlbumById(id);
        return enrichAlbumWithSongs(album);
    }

    public AlbumDto createAlbum(AlbumDto albumDTO) {
        if (albumDTO == null) {
            throw new IllegalArgumentException("Album data cannot be null");
        }

        Album album = albumMapper.toEntity(albumDTO);
        Album savedAlbum = albumService.createAlbum(album);
        return enrichAlbumWithSongs(savedAlbum);
    }

    public AlbumDto updateAlbum(Long id, AlbumDto albumDto) {
        if (id == null) {
            throw new IllegalArgumentException("Album ID cannot be null");
        }

        if (albumDto == null) {
            throw new IllegalArgumentException("Album data cannot be null");
        }

        Album updatedAlbum = albumService.updateAlbum(id, albumMapper.toEntity(albumDto));
        return enrichAlbumWithSongs(updatedAlbum);
    }

    public void deleteAlbum(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Album ID cannot be null");
        }

        albumService.deleteAlbum(id);
    }

    private AlbumDto enrichAlbumWithSongs(Album album) {
        AlbumDto albumDTO = albumMapper.toDto(album);
        List<SongDto> songs = songFacade.getSongsByAlbum(album.getId());
        albumDTO.setSongs(songs);
        return albumDTO;
    }
}