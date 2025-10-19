package com.example.APIDemo.controller;

import com.example.APIDemo.dto.FilmRequest;
import com.example.APIDemo.dto.FilmResponse;
import com.example.APIDemo.exception.BusinessException;
import com.example.APIDemo.exception.NotFoundException;
import com.example.APIDemo.service.IFilmService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class FilmController {

    private final IFilmService filmService;

    public FilmController(IFilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public ResponseEntity<?> getAllFilms() {
        try {
            List<FilmResponse> films = filmService.getAllFilms();
            return ResponseEntity.ok(films);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(simpleError(500, "Internal Server Error"));
        }
    }

    @GetMapping("/films/{id}")
    public ResponseEntity<?> getFilmById(@PathVariable Long id) {
        try {
            FilmResponse res = filmService.getFilmById(id);
            return ResponseEntity.ok(res);
        } catch (NotFoundException nfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(simpleError(404, "Not found"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(simpleError(500, "Internal Server Error"));
        }
    }

    @PostMapping("/films")
    public ResponseEntity<?> createFilm(@RequestBody FilmRequest req) {
        try {
            FilmResponse created = filmService.createFilm(req);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (BusinessException be) {
            return ResponseEntity.badRequest().body(simpleError(400, "Invalid request"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(simpleError(500, "Internal Server Error"));
        }
    }

    @PutMapping("/films/{id}")
    public ResponseEntity<?> updateFilm(@PathVariable Long id, @RequestBody FilmRequest req) {
        try {
            FilmResponse updated = filmService.updateFilm(id, req);
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

    @DeleteMapping("/films/{id}")
    public ResponseEntity<?> deleteFilm(@PathVariable Long id) {
        try {
            filmService.deleteFilm(id);
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
