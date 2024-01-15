package ru.practicum.exploreWithMe.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.exploreWithMe.entity.ParticipationRequest;

import java.util.List;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Integer> {

    @Query(value = "SELECT pr FROM ParticipationRequest AS pr WHERE pr.event.id = :eventId " +
            "AND pr.requester.id = :userId")
    ParticipationRequest searchDuplicateRequest(@Param("userId") int userId, @Param("eventId") int eventId);

    @Query(value = "SELECT pr FROM ParticipationRequest AS pr WHERE pr.requester.id = :userId")
    List<ParticipationRequest> getUserRequest(@Param("userId") int userId);


    @Query(value = "SELECT pr FROM ParticipationRequest AS pr WHERE pr.event.id = :eventId")
    List<ParticipationRequest> findAllRequestsByEventId(@Param("eventId") int eventId);

    @Query(value = "SELECT pr FROM ParticipationRequest AS pr WHERE pr.id IN(:requestIds) ")
    List<ParticipationRequest> findByRequestIds(@Param("requestIds") List<Integer> requestIds);
}
