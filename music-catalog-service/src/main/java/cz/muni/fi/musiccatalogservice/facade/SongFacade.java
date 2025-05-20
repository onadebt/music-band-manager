package cz.muni.fi.musiccatalogservice.facade;

import cz.muni.fi.musiccatalogservice.dto.SongDto;
import cz.muni.fi.musiccatalogservice.mapper.SongMapper;
import cz.muni.fi.musiccatalogservice.model.Album;
import cz.muni.fi.musiccatalogservice.model.Song;
import cz.muni.fi.musiccatalogservice.service.AlbumService;
import cz.muni.fi.musiccatalogservice.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
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

    public List<SongDto> getAllSongs() {
        return songService.getAllSongs().stream()
                .map(songMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<SongDto> getSongsByAlbum(Long albumId) {
        if (albumId == null) {
            throw new IllegalArgumentException("Album ID cannot be null");
        }

        return songService.getSongsByAlbum(albumId).stream()
                .map(songMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<SongDto> getSongsByBand(Long bandId) {
        if (bandId == null) {
            throw new IllegalArgumentException("Band ID cannot be null");
        }

        List<Song> songs = songService.getSongsByBand(bandId);
        return songs.stream()
                .map(songMapper::toDto)
                .collect(Collectors.toList());
    }

    public SongDto getSongById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Song ID cannot be null");
        }

        Song song = songService.getSongById(id);
        return songMapper.toDto(song);
    }

    public SongDto createSong(SongDto songDto) {
        if (songDto == null) {
            throw new IllegalArgumentException("Song data cannot be null");
        }

        Song song = songMapper.toEntity(songDto);

        if (songDto.getAlbumId() != null) {
            Album album = albumService.getAlbumById(songDto.getAlbumId());
            song.setAlbum(album);
        }

        Song savedSong = songService.createSong(song);
        return songMapper.toDto(savedSong);
    }

    public SongDto updateSong(Long id, SongDto songDTO) {
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
        return songMapper.toDto(updatedSong);
    }

    public void deleteSong(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Song ID cannot be null");
        }

        songService.deleteSong(id);
    }
}