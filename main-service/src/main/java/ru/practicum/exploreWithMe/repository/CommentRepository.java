package ru.practicum.exploreWithMe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.exploreWithMe.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query(value = "SELECT c FROM Comment AS c WHERE c.user.id = :userId AND c.event.id = :eventId")
    public Comment findByUserEvent(@Param("userId") int userId, @Param("eventId") int eventId);
}
