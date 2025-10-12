package com.example.APIDemo.service;

import com.example.APIDemo.dto.ActorRequest;
import com.example.APIDemo.dto.ActorResponse;

import java.util.List;

public interface IActorService {
    List<ActorResponse> getAllActors();
    ActorResponse getActorById(Long id);
    ActorResponse createActor(ActorRequest req);
    ActorResponse updateActor(Long id, ActorRequest req);
    boolean deleteActor(Long id);
}