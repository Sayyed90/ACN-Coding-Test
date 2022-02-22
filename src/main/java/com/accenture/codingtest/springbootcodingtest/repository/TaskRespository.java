package com.accenture.codingtest.springbootcodingtest.repository;

import com.accenture.codingtest.springbootcodingtest.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRespository extends JpaRepository<Task,Long> {
    @Query("SELECT t FROM Task t Where t.user= :ids")
    Optional<Task> findByUserLoggedInID(@Param("ids")long ids);
}
