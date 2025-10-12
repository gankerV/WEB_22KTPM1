
package com.example.APIDemo.service.implement;

import com.example.APIDemo.dto.ActorRequest;
import com.example.APIDemo.dto.ActorResponse;
import com.example.APIDemo.model.Actor;
import com.example.APIDemo.repository.IActorRepository;
import com.example.APIDemo.service.IActorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActorService implements IActorService {

    private final IActorRepository repo;

    public ActorService(IActorRepository repo) {
        this.repo = repo;
    }

    private ActorResponse toResponse(Actor a) {
        return new ActorResponse(a.getId(), a.getFirstName(), a.getLastName());
    }

    private void copyFromRequest(ActorRequest req, Actor target) {
        target.setFirstName(req.getFirstName());
        target.setLastName(req.getLastName());
    }

    @Override
    public List<ActorResponse> getAllActors() {
        return repo.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public ActorResponse getActorById(Long id) {
        return repo.findById(id).map(this::toResponse).orElse(null);
    }

    @Override
    public ActorResponse createActor(ActorRequest req) {
        Actor a = new Actor();
        copyFromRequest(req, a);
        a = repo.save(a);
        return toResponse(a);
    }

    @Override
    public ActorResponse updateActor(Long id, ActorRequest req) {
        return repo.findById(id).map(existing -> {
            copyFromRequest(req, existing);
            return toResponse(repo.save(existing));
        }).orElse(null);
    }

    @Override
    public boolean deleteActor(Long id) {
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }
}
