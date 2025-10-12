package com.example.APIDemo.service;

import java.util.List;

import com.example.APIDemo.model.Actor;

public interface IActorService {
    List<Actor> getAllActors();
    Actor getActorById(Long id);
    Actor createActor(Actor actor);
    boolean deleteActor(Long id);
    Actor updateActor(Long id, Actor actor);
}

