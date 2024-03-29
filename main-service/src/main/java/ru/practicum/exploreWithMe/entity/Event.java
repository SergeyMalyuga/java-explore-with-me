package ru.practicum.exploreWithMe.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Column(name = "description")
    private String description;
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;
    @Column(name = "paid", nullable = false)
    private boolean paid;
    @Column(name = "participant_limit")
    private int participantLimit;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @Column(name = "title", nullable = false)
    private String title;
    @Formula(value = "(SELECT COUNT(r.id) FROM requests AS r WHERE r.event_id = id AND r.request_status LIKE 'CONFIRMED')")
    private int confirmedRequests;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Enumerated(EnumType.STRING)
    private EventState state;
    @OneToMany(mappedBy = "event")
    private List<Comment> commentsList;
    @Transient
    private Long views;
}
