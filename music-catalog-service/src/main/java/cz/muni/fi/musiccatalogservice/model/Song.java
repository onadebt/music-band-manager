package cz.muni.fi.musiccatalogservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int duration;
    private Long bandId;

    @ManyToOne
    @JoinColumn(name = "album_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Album album;
}