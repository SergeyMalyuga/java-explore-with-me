package ru.practicum.exploreWithMe.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.exploreWithMe.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT u FROM User AS u WHERE (:ids IS NULL OR u.id IN (:ids))")
    List<User> findAllUsersWithPagination(@Param("ids") List<Integer> uid, Pageable pageable);

    User findByEmail(String email);
}
