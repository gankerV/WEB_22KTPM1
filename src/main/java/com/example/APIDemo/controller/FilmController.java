package com.example.APIDemo.controller;

import com.example.APIDemo.dto.FilmRequest;
import com.example.APIDemo.dto.FilmResponse;
import com.example.APIDemo.service.IFilmService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class FilmController {

    private final IFilmService filmService;

    public FilmController(IFilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public ResponseEntity<List<FilmResponse>> getAllFilms() {
        return ResponseEntity.ok(filmService.getAllFilms());
    }

    @GetMapping("/films/{id}")
    public ResponseEntity<?> getFilmById(@PathVariable Long id) {
        if (id == null || id < 1) return badRequest("id", "id phải là số dương");
        FilmResponse res = filmService.getFilmById(id);
        if (res == null) return notFound("Không tìm thấy film id=" + id);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/films")
    public ResponseEntity<?> createFilm(@Valid @RequestBody FilmRequest req, BindingResult br) {
        if (br.hasErrors()) return badRequest(br);
        return ResponseEntity.status(HttpStatus.CREATED).body(filmService.createFilm(req));
    }

    @PutMapping("/films/{id}")
    public ResponseEntity<?> updateFilm(@PathVariable Long id,
                                        @Valid @RequestBody FilmRequest req,
                                        BindingResult br) {
        if (br.hasErrors()) return badRequest(br);
        if (id == null || id < 1) return badRequest("id", "id phải là số dương");
        FilmResponse updated = filmService.updateFilm(id, req);
        if (updated == null) return notFound("Không tìm thấy film id=" + id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/films/{id}")
    public ResponseEntity<?> deleteFilm(@PathVariable Long id) {
        if (id == null || id < 1) return badRequest("id", "id phải là số dương");
        boolean ok = filmService.deleteFilm(id);
        if (!ok) return notFound("Không tìm thấy film id=" + id + " hoặc không xóa được");
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<Map<String, Object>> badRequest(BindingResult br) {
        var details = br.getFieldErrors().stream()
                .map(e -> Map.of("field", e.getField(), "message", e.getDefaultMessage()))
                .collect(Collectors.toList());
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", 400);
        body.put("error", "Bad Request");
        body.put("message", "Dữ liệu không hợp lệ");
        body.put("details", details);
        return ResponseEntity.badRequest().body(body);
    }

    private ResponseEntity<Map<String, Object>> badRequest(String field, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", 400);
        body.put("error", "Bad Request");
        body.put("message", "Dữ liệu không hợp lệ");
        body.put("details", List.of(Map.of("field", field, "message", message)));
        return ResponseEntity.badRequest().body(body);
    }

    private ResponseEntity<Map<String, Object>> notFound(String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", 404);
        body.put("error", "Not Found");
        body.put("message", message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
