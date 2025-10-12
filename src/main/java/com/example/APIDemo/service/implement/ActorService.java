package com.example.APIDemo.service.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.APIDemo.model.Actor;
import com.example.APIDemo.service.IActorService;
import com.example.APIDemo.repository.IActorRepository;

@Service
public class ActorService implements IActorService {
    @Autowired
    IActorRepository _actorRepository;


    @Override
    public List<Actor> getAllActors() {
        return _actorRepository.findAll();
    }

    @Override
    public Actor getActorById(Long id) {
        return _actorRepository.findById(id).orElse(null);
    }

    @Override
    public Actor createActor(Actor actor) {
        return _actorRepository.save(actor);
    }

    @Override
    public boolean deleteActor(Long id) {
        if (_actorRepository.findById(id).isPresent()) {
            _actorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Actor updateActor(Long id, Actor actor) {
        if (_actorRepository.findById(id).isPresent()) {
            Actor existingActor = _actorRepository.findById(id).get();
            existingActor.setFirstName(actor.getFirstName());
            existingActor.setLastName(actor.getLastName());
            _actorRepository.save(existingActor);
            return _actorRepository.findById(id).get();
        }
        return null;
    }
}
