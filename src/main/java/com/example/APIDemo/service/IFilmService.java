package com.example.APIDemo.service;

import com.example.APIDemo.dto.FilmRequest;
import com.example.APIDemo.dto.FilmResponse;
import com.example.APIDemo.model.Film;
import java.util.List;

public interface IFilmService {
    List<FilmResponse> getAllFilms();
    FilmResponse getFilmById(Long id);            // null nếu không thấy
    FilmResponse createFilm(FilmRequest req);
    FilmResponse updateFilm(Long id, FilmRequest req); // null nếu không thấy
    boolean deleteFilm(Long id);
}