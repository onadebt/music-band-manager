package cz.muni.fi.musiccatalogservice.facade;

import cz.muni.fi.musiccatalogservice.dto.SongDTO;
import cz.muni.fi.musiccatalogservice.mapper.SongMapper;
import cz.muni.fi.musiccatalogservice.model.Album;
import cz.muni.fi.musiccatalogservice.model.Song;
import cz.muni.fi.musiccatalogservice.service.AlbumService;
import cz.muni.fi.musiccatalogservice.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SongFacade {

    private final SongService songService;
    private final AlbumService albumService;
    private final SongMapper songMapper;

    @Autowired
    public SongFacade(SongService songService, AlbumService albumService,
                      SongMapper songMapper) {
        this.songService = songService;
        this.albumService = albumService;
        this.songMapper = songMapper;
    }

    public List<SongDTO> getAllSongs() {
        return songService.getAllSongs().stream()
                .map(songMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<SongDTO> getSongsByAlbum(Long albumId) {
        if (albumId == null) {
            throw new IllegalArgumentException("Album ID cannot be null");
        }

        return songService.getSongsByAlbum(albumId).stream()
                .map(songMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<SongDTO> getSongsByBand(Long bandId) {
        if (bandId == null) {
            throw new IllegalArgumentException("Band ID cannot be null");
        }

        List<Song> songs = songService.getSongsByBand(bandId);
        if (songs == null) {
            throw new IllegalArgumentException("Invalid band ID: " + bandId);
        }

        return songs.stream()
                .map(songMapper::toDTO)
                .collect(Collectors.toList());
    }

    public SongDTO getSongById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Song ID cannot be null");
        }

        Song song = songService.getSongById(id);
        if (song == null) {
            throw new IllegalArgumentException("Song not found with ID: " + id);
        }

        return songMapper.toDTO(song);
    }

    public SongDTO createSong(SongDTO songDTO) {
        if (songDTO == null) {
            throw new IllegalArgumentException("Song data cannot be null");
        }

        Song song = songMapper.toEntity(songDTO);

        if (songDTO.getAlbumId() != null) {
            Album album = albumService.getAlbumById(songDTO.getAlbumId());
            song.setAlbum(album);
        }

        Song savedSong = songService.createSong(song);
        return songMapper.toDTO(savedSong);
    }

    public SongDTO updateSong(Long id, SongDTO songDTO) {
        if (id == null) {
            throw new IllegalArgumentException("Song ID cannot be null");
        }

        if (songDTO == null) {
            throw new IllegalArgumentException("Song data cannot be null");
        }

        Song song = songService.getSongById(id);
        songMapper.updateEntityFromDto(songDTO, song);

        if (songDTO.getAlbumId() != null) {
            Album album = albumService.getAlbumById(songDTO.getAlbumId());
            song.setAlbum(album);
        } else {
            song.setAlbum(null);
        }

        Song updatedSong = songService.createSong(song);
        return songMapper.toDTO(updatedSong);
    }

    public void deleteSong(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Song ID cannot be null");
        }

        Song song = songService.getSongById(id);
        if (song == null) {
            throw new IllegalArgumentException("Song not found with ID: " + id);
        }

        songService.deleteSong(id);
    }
}