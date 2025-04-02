package cz.muni.fi.musiccatalogservice.service;

import cz.muni.fi.musiccatalogservice.dto.SongDTO;
import cz.muni.fi.musiccatalogservice.model.Album;
import cz.muni.fi.musiccatalogservice.model.Song;
import cz.muni.fi.musiccatalogservice.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SongService {

    private final SongRepository songRepository;
    private final AlbumService albumService;

    @Autowired
    public SongService(SongRepository songRepository, @Lazy AlbumService albumService) {
        this.songRepository = songRepository;
        this.albumService = albumService;
    }

    public List<SongDTO> getAllSongs() {
        return songRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SongDTO> getSongsByAlbum(Long albumId) {
        return songRepository.findByAlbumId(albumId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SongDTO> getSongsByBand(Long bandId) {
        return songRepository.findByBandId(bandId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<SongDTO> getSongById(Long id) {
        return songRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Transactional
    public SongDTO createSong(SongDTO songDTO) {
        Song song = convertToEntity(songDTO);

        // Set album relationship if albumId provided
        if (songDTO.getAlbumId() != null) {
            Optional<Album> album = albumService.getAlbumEntityById(songDTO.getAlbumId());
            album.ifPresent(song::setAlbum);
        }

        Song savedSong = songRepository.save(song);
        return convertToDTO(savedSong);
    }

    @Transactional
    public SongDTO updateSong(Long id, SongDTO songDTO) {
        Optional<Song> existingSong = songRepository.findById(id);

        if (existingSong.isPresent()) {
            Song song = existingSong.get();
            song.setName(songDTO.getName());
            song.setDuration(songDTO.getDuration());
            song.setBandId(songDTO.getBandId());

            if (songDTO.getAlbumId() != null) {
                Optional<Album> album = albumService.getAlbumEntityById(songDTO.getAlbumId());
                album.ifPresent(song::setAlbum);
            } else {
                song.setAlbum(null);
            }

            Song updatedSong = songRepository.save(song);
            return convertToDTO(updatedSong);
        }

        return null;
    }

    @Transactional
    public boolean deleteSong(Long id) {
        if (songRepository.existsById(id)) {
            songRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private SongDTO convertToDTO(Song song) {
        SongDTO songDTO = new SongDTO();
        songDTO.setId(song.getId());
        songDTO.setName(song.getName());
        songDTO.setDuration(song.getDuration());
        songDTO.setBandId(song.getBandId());

        if (song.getAlbum() != null) {
            songDTO.setAlbumId(song.getAlbum().getId());
        }

        return songDTO;
    }

    private Song convertToEntity(SongDTO songDTO) {
        Song song = new Song();
        song.setName(songDTO.getName());
        song.setDuration(songDTO.getDuration());
        song.setBandId(songDTO.getBandId());
        return song;
    }
}