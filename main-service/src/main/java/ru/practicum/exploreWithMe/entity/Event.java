package ru.practicum.exploreWithMe.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "annotation", nullable = false)
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Column(name = "description")
    private String description;
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;
    @Column(name = "paid", nullable = false)
    private boolean paid;
    @Column(name = "participant_limit")
    private int participantLimit;
    @Column(name = "request_moderation")
    private boolean requestModeration;
    @Column(name = "title", nullable = false)
    private String title;
    @Transient //TODO дописать заполнение поля из таблицы Requests
    private int confirmedRequests;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Enumerated(EnumType.STRING)
    private EventState state;
    @Transient
    private int views;
}
