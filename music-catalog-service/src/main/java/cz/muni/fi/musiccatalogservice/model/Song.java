package cz.muni.fi.musiccatalogservice.model;

import jakarta.persistence.*;

@Entity
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int duration;
    private Long bandId;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public Long getBandId() {
        return bandId;
    }

    public Album getAlbum() {
        return album;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setBandId(Long bandId) {
        this.bandId = bandId;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}