package project.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class WasteBin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "fill_level")
    private int fillLevel;

    @Column(name = "last_fill_level")
    private int lastFillLevel;

    @Column(name = "growth_rate")
    private double growthRate;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
}