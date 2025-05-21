package cz.muni.fi.musiccatalogservice.facade;

import cz.muni.fi.musiccatalogservice.dto.AlbumDto;
import cz.muni.fi.musiccatalogservice.dto.SongDto;
import cz.muni.fi.musiccatalogservice.mapper.AlbumMapper;
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
public class AlbumFacade {

    private final AlbumService albumService;
    private final SongService songService;
    private final AlbumMapper albumMapper;
    private final SongMapper songMapper;

    @Autowired
    public AlbumFacade(AlbumService albumService, SongService songService,
                       AlbumMapper albumMapper, SongMapper songMapper) {
        this.albumService = albumService;
        this.songService = songService;
        this.albumMapper = albumMapper;
        this.songMapper = songMapper;
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

    public AlbumDto createAlbum(AlbumDto albumDto) {
        if (albumDto == null) {
            throw new IllegalArgumentException("Album data cannot be null");
        }

        Album album = albumMapper.toEntity(albumDto);
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
        AlbumDto albumDto = albumMapper.toDto(album);
        List<SongDto> songs = songService.getSongsByAlbum(album.getId()).stream()
                .map(songMapper::toDto)
                .collect(Collectors.toList());
        albumDto.setSongs(songs);
        return albumDto;
    }

    public SongDto addSongToAlbum(Long albumId, SongDto songDto) {
        if (albumId == null) {
            throw new IllegalArgumentException("Album ID cannot be null");
        }

        if (songDto == null) {
            throw new IllegalArgumentException("Song data cannot be null");
        }

        Album album = albumService.getAlbumById(albumId);
        Song song;

        if (songDto.getId() != null) {
            song = songService.getSongById(songDto.getId());
            song.setAlbum(album);
        } else {
            song = songMapper.toEntity(songDto);
            song.setAlbum(album);
        }

        song.setBandId(album.getBandId());
        album.addSong(song);

        Album updatedAlbum = albumService.updateAlbum(albumId, album);
        Song savedSong = updatedAlbum.getSongs().stream()
                .filter(s -> s.getName().equals(song.getName()) && s.getDuration() == song.getDuration())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Failed to retrieve saved song"));

        return songMapper.toDto(savedSong);
    }

    public void removeSongFromAlbum(Long albumId, Long songId) {
        if (albumId == null) {
            throw new IllegalArgumentException("Album ID cannot be null");
        }

        if (songId == null) {
            throw new IllegalArgumentException("Song ID cannot be null");
        }

        Album album = albumService.getAlbumById(albumId);
        Song song = songService.getSongById(songId);

        if (song.getAlbum() == null) {
            throw new IllegalArgumentException("Song is not associated with any album");
        }

        if (!album.getId().equals(song.getAlbum().getId())) {
            throw new IllegalArgumentException("Song does not belong to the specified album");
        }

        album.removeSong(song);

        songService.updateSong(songId, song);
        albumService.updateAlbum(albumId, album);
    }
}