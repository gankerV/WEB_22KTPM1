package com.example.APIDemo.service.implement;

import com.example.APIDemo.dto.FilmRequest;
import com.example.APIDemo.dto.FilmResponse;
import com.example.APIDemo.model.Film;
import com.example.APIDemo.repository.IFilmRepository;
import com.example.APIDemo.service.IFilmService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FilmService implements IFilmService {

    private final IFilmRepository filmRepository;

    public FilmService(IFilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    private FilmResponse toResponse(Film f) {
        return new FilmResponse(
                f.getId(), f.getTitle(), f.getDescription(), f.getReleaseYear(),
                f.getRentalDuration(), f.getRentalRate(), f.getLength(),
                f.getReplacementCost(), f.getRating(), f.getSpecialFeatures(),
                f.getLastUpdate()
        );
    }

    private void copyFromRequest(FilmRequest req, Film target) {
        target.setTitle(req.getTitle());
        target.setDescription(req.getDescription());
        target.setReleaseYear(req.getReleaseYear());
        // ❗ BỎ QUA validate/mapping language/originalLanguage theo yêu cầu
        target.setRentalDuration(req.getRentalDuration());
        target.setRentalRate(req.getRentalRate());
        target.setLength(req.getLength());
        target.setReplacementCost(req.getReplacementCost());
        target.setRating(req.getRating());
        target.setSpecialFeatures(req.getSpecialFeatures());
        target.setLastUpdate(LocalDateTime.now());
    }

    @Override
    public List<FilmResponse> getAllFilms() {
        return filmRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public FilmResponse getFilmById(Long id) {
        return filmRepository.findById(id).map(this::toResponse).orElse(null);
    }

    @Override
    public FilmResponse createFilm(FilmRequest req) {
        Film f = new Film();
        copyFromRequest(req, f);
        return toResponse(filmRepository.save(f));
    }

    @Override
    public FilmResponse updateFilm(Long id, FilmRequest req) {
        return filmRepository.findById(id).map(ex -> {
            copyFromRequest(req, ex);
            return toResponse(filmRepository.save(ex));
        }).orElse(null);
    }

    @Override
    public boolean deleteFilm(Long id) {
        if (!filmRepository.existsById(id)) return false;
        filmRepository.deleteById(id);
        return true;
    }
}
