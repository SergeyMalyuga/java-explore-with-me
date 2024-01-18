package ru.practicum.exploreWithMe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.exploreWithMe.entity.Comment;
import ru.practicum.exploreWithMe.entity.Event;
import ru.practicum.exploreWithMe.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query(value = "SELECT c FROM Comment AS c WHERE c.user.id = :userId AND c.event.id = :eventId")
    Comment findByUserEvent(@Param("userId") int userId, @Param("eventId") int eventId);

    List<Comment> findCommentsByEvent(Event event, Pageable pageable);

    Page<Comment> findAll(Pageable pageable);

    List<Comment> findAllByUser(User user, Pageable pageable);

    @Query(value = "SELECT c FROM Comment AS c WHERE (:userIdList IS NULL OR c.user.id IN (:userIdList)) " +
            "AND (:eventIdList IS NULL OR c.event.id IN (:eventIdList))" +
            "AND (COALESCE(:rangeStart, null) IS NULL OR c.createdOn >= :rangeStart) " +
            "AND (COALESCE(:rangeEnd, null) IS NULL OR c.createdOn < :rangeEnd) ")
    Page<Comment> findCommentsByAdmin(@Param("userIdList") List<Integer> commentIdList,
                                      @Param("eventIdList") List<Integer> eventIdList,
                                      @Param("rangeStart") LocalDateTime rangeStart,
                                      @Param("rangeEnd") LocalDateTime rangeEnd,
                                      Pageable pageable);
}
