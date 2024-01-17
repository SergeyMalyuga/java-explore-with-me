package ru.practicum.exploreWithMe.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name = "locations")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "lat")
    private double lat;
    @Column(name = "lon")
    private double lon;
}
