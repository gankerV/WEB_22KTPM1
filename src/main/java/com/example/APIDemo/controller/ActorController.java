package com.example.APIDemo.controller;

import com.example.APIDemo.dto.ActorRequest;
import com.example.APIDemo.dto.ActorResponse;
import com.example.APIDemo.exception.BusinessException;
import com.example.APIDemo.exception.NotFoundException;
import com.example.APIDemo.service.IActorService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ActorController {

    private final IActorService actorService;

    public ActorController(IActorService actorService) {
        this.actorService = actorService;
    }

    @GetMapping("/actors")
    public ResponseEntity<?> getAllActors() {
        try {
            List<ActorResponse> actors = actorService.getAllActors();
            return ResponseEntity.ok(actors);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(simpleError(500, "Internal Server Error"));
        }
    }

    @GetMapping("/actors/{id}")
    public ResponseEntity<?> getActorById(@PathVariable Long id) {
        try {
            ActorResponse res = actorService.getActorById(id);
            return ResponseEntity.ok(res);
        } catch (NotFoundException nfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(simpleError(404, "Not found"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(simpleError(500, "Internal Server Error"));
        }
    }

    @PostMapping("/actors")
    public ResponseEntity<?> createActor(@RequestBody ActorRequest req) {
        try {
            ActorResponse created = actorService.createActor(req);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (BusinessException be) {
            return ResponseEntity.badRequest().body(simpleError(400, "Invalid request"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(simpleError(500, "Internal Server Error"));
        }
    }

    @PutMapping("/actors/{id}")
    public ResponseEntity<?> updateActor(@PathVariable Long id, @RequestBody ActorRequest req) {
        try {
            ActorResponse updated = actorService.updateActor(id, req);
            return ResponseEntity.ok(updated);
        } catch (NotFoundException nfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(simpleError(404, "Not found"));
        } catch (BusinessException be) {
            return ResponseEntity.badRequest().body(simpleError(400, "Invalid request"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(simpleError(500, "Internal Server Error"));
        }
    }

    @DeleteMapping("/actors/{id}")
    public ResponseEntity<?> deleteActor(@PathVariable Long id) {
        try {
            actorService.deleteActor(id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException nfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(simpleError(404, "Not found"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(simpleError(500, "Internal Server Error"));
        }
    }

    private static Map<String, Object> simpleError(int status, String message) {
        return Map.of("status", status, "message", message);
    }
}
