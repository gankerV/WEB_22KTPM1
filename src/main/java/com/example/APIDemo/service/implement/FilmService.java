package com.example.APIDemo.service.implement;

import com.example.APIDemo.model.Film;
import com.example.APIDemo.repository.IFilmRepository;
import com.example.APIDemo.service.IFilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FilmService implements IFilmService {

    @Autowired
    IFilmRepository filmRepository;

    @Override
    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }

    @Override
    public Film getFilmById(Long id) {
        return filmRepository.findById(id).orElse(null);
    }

    @Override
    public Film createFilm(Film film) {
        return filmRepository.save(film);
    }

    @Override
    public Film updateFilm(Long id, Film film) {
        Film existing = filmRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setTitle(film.getTitle());
            existing.setDescription(film.getDescription());
            existing.setReleaseYear(film.getReleaseYear());
            existing.setLanguage(film.getLanguage());
            existing.setOriginalLanguage(film.getOriginalLanguage());
            existing.setRentalDuration(film.getRentalDuration());
            existing.setRentalRate(film.getRentalRate());
            existing.setLength(film.getLength());
            existing.setReplacementCost(film.getReplacementCost());
            existing.setRating(film.getRating());
            existing.setSpecialFeatures(film.getSpecialFeatures());
            existing.setLastUpdate(film.getLastUpdate());
            return filmRepository.save(existing);
        }
        return null;
    }

    @Override
    public boolean deleteFilm(Long id) {
        if (filmRepository.existsById(id)) {
            filmRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
