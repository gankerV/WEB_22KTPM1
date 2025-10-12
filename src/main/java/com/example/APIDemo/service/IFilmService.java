package com.example.APIDemo.service;

import com.example.APIDemo.model.Film;
import java.util.List;

public interface IFilmService {
    List<Film> getAllFilms();
    Film getFilmById(Long id);
    Film createFilm(Film film);
    Film updateFilm(Long id, Film film);
    boolean deleteFilm(Long id);
}
