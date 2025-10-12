package com.example.APIDemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.APIDemo.model.Film;

public interface IFilmRepository extends JpaRepository<Film, Long> {

}
