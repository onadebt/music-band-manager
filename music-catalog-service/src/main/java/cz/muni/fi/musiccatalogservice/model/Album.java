package cz.muni.fi.musiccatalogservice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private LocalDateTime releaseDate;
    private Long bandId;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Song> songs = new ArrayList<>();

    public Album() {
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    public Long getBandId() {
        return bandId;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setBandId(Long bandId) {
        this.bandId = bandId;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public void addSong(Song song) {
        songs.add(song);
        song.setAlbum(this);
    }

    public void removeSong(Song song) {
        songs.remove(song);
        song.setAlbum(null);
    }
}