package ru.practicum.exploreWithMe.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.exploreWithMe.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT u FROM User AS u WHERE u.id IN (:ids)")
    List<User> findAllUsers(@Param("ids") List<Integer> uid);

    @Query(value = "SELECT u FROM User AS u")
    Page<User> findAllUsersWithPagination(Pageable pageable);

    User findByEmail(String email);
}
