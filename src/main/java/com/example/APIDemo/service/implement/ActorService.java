package com.example.APIDemo.service.implement;

import com.example.APIDemo.dto.ActorRequest;
import com.example.APIDemo.dto.ActorResponse;
import com.example.APIDemo.exception.BusinessException;
import com.example.APIDemo.exception.NotFoundException;
import com.example.APIDemo.model.Actor;
import com.example.APIDemo.repository.IActorRepository;
import com.example.APIDemo.service.IActorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActorService implements IActorService {

    private static final Logger LOG_MAIN  = LoggerFactory.getLogger("MAIN");
    private static final Logger LOG_ERROR = LoggerFactory.getLogger("ERROR");

    private final IActorRepository repo;

    public ActorService(IActorRepository repo) {
        this.repo = repo;
    }

    private void validateRequest(ActorRequest req) {
        if (req == null) {
            LOG_ERROR.error("[ACTOR][VALIDATION] Request body is null");
            throw new BusinessException("Invalid request");
        }
        if (req.getFirstName() == null || req.getFirstName().isBlank()) {
            LOG_ERROR.error("[ACTOR][VALIDATION] firstName is missing or blank");
            throw new BusinessException("Invalid request");
        }
        if (req.getLastName() == null || req.getLastName().isBlank()) {
            LOG_ERROR.error("[ACTOR][VALIDATION] lastName is missing or blank");
            throw new BusinessException("Invalid request");
        }
    }

    private ActorResponse toResponse(Actor a) {
        LOG_MAIN.info("[ACTOR][MAP->Response] actor={}", a);
        return new ActorResponse(a.getId(), a.getFirstName(), a.getLastName());
    }

    private void copyFromRequest(ActorRequest req, Actor target) {
        LOG_MAIN.info("[ACTOR][COPY FROM REQ] req={}, before={}", req, target);
        target.setFirstName(req.getFirstName());
        target.setLastName(req.getLastName());
    }

    @Override
    public List<ActorResponse> getAllActors() {
        try {
            var list = repo.findAll();
            LOG_MAIN.info("[ACTOR][DB] fetched_count={}", list.size());
            return list.stream().map(this::toResponse).collect(Collectors.toList());
        } catch (Exception ex) {
            LOG_ERROR.error("[ACTOR][SERVICE][GET ALL] error", ex);
            throw new RuntimeException("Unexpected error");
        }
    }

    @Override
    public ActorResponse getActorById(Long id) {
        try {
            Actor actor = repo.findById(id)
                    .orElseThrow(() -> new NotFoundException("Actor not found"));
            LOG_MAIN.info("[ACTOR][DB] found id={}, actor={}", id, actor);
            return toResponse(actor);
        } catch (NotFoundException nfe) {
            LOG_ERROR.error("[ACTOR][SERVICE][GET] not found id={}", id);
            throw nfe;
        } catch (Exception ex) {
            LOG_ERROR.error("[ACTOR][SERVICE][GET] error id={}", id, ex);
            throw new RuntimeException("Unexpected error");
        }
    }

    @Override
    public ActorResponse createActor(ActorRequest req) {
        try {
            validateRequest(req);
            Actor a = new Actor();
            copyFromRequest(req, a);
            a = repo.save(a);
            LOG_MAIN.info("[ACTOR][DB] saved actor={}", a);
            return toResponse(a);
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            LOG_ERROR.error("[ACTOR][SERVICE][CREATE] error req={}", req, ex);
            throw new RuntimeException("Unexpected error");
        }
    }

    @Override
    public ActorResponse updateActor(Long id, ActorRequest req) {
        try {
            validateRequest(req);
            Actor existing = repo.findById(id)
                    .orElseThrow(() -> new NotFoundException("Actor not found"));
            copyFromRequest(req, existing);
            existing = repo.save(existing);
            LOG_MAIN.info("[ACTOR][DB] updated actor={}", existing);
            return toResponse(existing);
        } catch (NotFoundException nfe) {
            LOG_ERROR.error("[ACTOR][SERVICE][UPDATE] not found id={}", id);
            throw nfe;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            LOG_ERROR.error("[ACTOR][SERVICE][UPDATE] error id={}, req={}", id, req, ex);
            throw new RuntimeException("Unexpected error");
        }
    }

    @Override
    public boolean deleteActor(Long id) {
        try {
            if (!repo.existsById(id)) {
                LOG_ERROR.error("[ACTOR][SERVICE][DELETE] not found id={}", id);
                throw new NotFoundException("Actor not found");
            }
            repo.deleteById(id);
            LOG_MAIN.info("[ACTOR][DB] deleted id={}", id);
            return true;
        } catch (NotFoundException nfe) {
            throw nfe;
        } catch (Exception ex) {
            LOG_ERROR.error("[ACTOR][SERVICE][DELETE] error id={}", id, ex);
            throw new RuntimeException("Unexpected error");
        }
    }
}
