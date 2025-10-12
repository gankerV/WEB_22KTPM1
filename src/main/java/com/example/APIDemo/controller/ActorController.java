package com.example.APIDemo.controller;

import com.example.APIDemo.dto.ActorRequest;
import com.example.APIDemo.dto.ActorResponse;
import com.example.APIDemo.service.IActorService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ActorController {

    private final IActorService actorService;

    public ActorController(IActorService actorService) {
        this.actorService = actorService;
    }

    @GetMapping("/actors")
    public ResponseEntity<List<ActorResponse>> getAllActors() {
        return ResponseEntity.ok(actorService.getAllActors());
    }

    @GetMapping("/actors/{id}")
    public ResponseEntity<?> getActorById(@PathVariable Long id) {
        ActorResponse res = actorService.getActorById(id);
        if (res == null) return basicNotFound("Actor id=" + id + " không tồn tại");
        return ResponseEntity.ok(res);
    }

    @PostMapping("/actors")
    public ResponseEntity<?> createActor(@Valid @RequestBody ActorRequest req, BindingResult br) {
        if (br.hasErrors()) return basicBadRequest(br);
        ActorResponse created = actorService.createActor(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/actors/{id}")
    public ResponseEntity<?> updateActor(@PathVariable Long id,
                                         @Valid @RequestBody ActorRequest req,
                                         BindingResult br) {
        if (br.hasErrors()) return basicBadRequest(br);
        ActorResponse updated = actorService.updateActor(id, req);
        if (updated == null) return basicNotFound("Actor id=" + id + " không tồn tại");
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/actors/{id}")
    public ResponseEntity<?> deleteActor(@PathVariable Long id) {
        boolean ok = actorService.deleteActor(id);
        if (!ok) return basicNotFound("Actor id=" + id + " không tồn tại hoặc không xóa được");
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<Map<String, Object>> basicBadRequest(BindingResult br) {
        var errors = br.getFieldErrors().stream()
                .map(e -> Map.of("field", e.getField(), "message", e.getDefaultMessage()))
                .collect(Collectors.toList());
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", 400);
        body.put("error", "Bad Request");
        body.put("message", "Dữ liệu không hợp lệ");
        body.put("details", errors);
        return ResponseEntity.badRequest().body(body);
    }

    private ResponseEntity<Map<String, Object>> basicNotFound(String msg) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", 404);
        body.put("error", "Not Found");
        body.put("message", msg);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
