package com.example.APIDemo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.APIDemo.model.Actor;

public interface IActorRepository extends JpaRepository<Actor, Long> {
    List<Actor> findAll();
    Optional<Actor> findById(long id);
}
