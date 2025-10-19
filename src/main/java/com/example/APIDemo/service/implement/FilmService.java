package com.example.APIDemo.service.implement;

import com.example.APIDemo.dto.FilmRequest;
import com.example.APIDemo.dto.FilmResponse;
import com.example.APIDemo.exception.BusinessException;
import com.example.APIDemo.exception.NotFoundException;
import com.example.APIDemo.model.Film;
import com.example.APIDemo.repository.IFilmRepository;
import com.example.APIDemo.service.IFilmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService implements IFilmService {

    private static final Logger LOG_MAIN  = LoggerFactory.getLogger("MAIN");
    private static final Logger LOG_ERROR = LoggerFactory.getLogger("ERROR");

    private final IFilmRepository filmRepository;

    public FilmService(IFilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    private void validateRequest(FilmRequest req) {
        if (req == null) {
            LOG_ERROR.error("[FILM][VALIDATION] Request body is null");
            throw new BusinessException("Invalid request");
        }
        if (req.getTitle() == null || req.getTitle().isBlank()) {
            LOG_ERROR.error("[FILM][VALIDATION] title is missing or blank");
            throw new BusinessException("Invalid request");
        }
        if (req.getReleaseYear() == null || req.getReleaseYear() < 1900) {
            LOG_ERROR.error("[FILM][VALIDATION] releaseYear is invalid");
            throw new BusinessException("Invalid request");
        }
        if (req.getRentalDuration() == null || req.getRentalDuration() <= 0) {
            LOG_ERROR.error("[FILM][VALIDATION] rentalDuration is invalid");
            throw new BusinessException("Invalid request");
        }
        if (req.getRentalRate() == null || req.getRentalRate().doubleValue() <= 0) {
            LOG_ERROR.error("[FILM][VALIDATION] rentalRate is invalid");
            throw new BusinessException("Invalid request");
        }
        // Thêm các validate khác tùy yêu cầu nghiệp vụ
    }

    private FilmResponse toResponse(Film f) {
        LOG_MAIN.info("[FILM][MAP->Response] film={}", f);
        return new FilmResponse(
                f.getId(), f.getTitle(), f.getDescription(), f.getReleaseYear(),
                f.getRentalDuration(), f.getRentalRate(), f.getLength(),
                f.getReplacementCost(), f.getRating(), f.getSpecialFeatures(),
                f.getLastUpdate()
        );
    }

    private void copyFromRequest(FilmRequest req, Film target) {
        LOG_MAIN.info("[FILM][COPY FROM REQ] req={}, before={}", req, target);
        target.setTitle(req.getTitle());
        target.setDescription(req.getDescription());
        target.setReleaseYear(req.getReleaseYear());
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
        try {
            var list = filmRepository.findAll();
            LOG_MAIN.info("[FILM][DB] fetched_count={}", list.size());
            return list.stream().map(this::toResponse).collect(Collectors.toList());
        } catch (Exception ex) {
            LOG_ERROR.error("[FILM][SERVICE][GET ALL] error", ex);
            throw new RuntimeException("Unexpected error");
        }
    }

    @Override
    public FilmResponse getFilmById(Long id) {
        try {
            Film film = filmRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Film not found"));
            LOG_MAIN.info("[FILM][DB] found id={}, film={}", id, film);
            return toResponse(film);
        } catch (NotFoundException nfe) {
            LOG_ERROR.error("[FILM][SERVICE][GET] not found id={}", id);
            throw nfe;
        } catch (Exception ex) {
            LOG_ERROR.error("[FILM][SERVICE][GET] error id={}", id, ex);
            throw new RuntimeException("Unexpected error");
        }
    }

    @Override
    public FilmResponse createFilm(FilmRequest req) {
        try {
            validateRequest(req);
            Film f = new Film();
            copyFromRequest(req, f);
            f = filmRepository.save(f);
            LOG_MAIN.info("[FILM][DB] saved film={}", f);
            return toResponse(f);
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            LOG_ERROR.error("[FILM][SERVICE][CREATE] error req={}", req, ex);
            throw new RuntimeException("Unexpected error");
        }
    }

    @Override
    public FilmResponse updateFilm(Long id, FilmRequest req) {
        try {
            validateRequest(req);
            Film existing = filmRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Film not found"));
            copyFromRequest(req, existing);
            existing = filmRepository.save(existing);
            LOG_MAIN.info("[FILM][DB] updated film={}", existing);
            return toResponse(existing);
        } catch (NotFoundException nfe) {
            LOG_ERROR.error("[FILM][SERVICE][UPDATE] not found id={}", id);
            throw nfe;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            LOG_ERROR.error("[FILM][SERVICE][UPDATE] error id={}, req={}", id, req, ex);
            throw new RuntimeException("Unexpected error");
        }
    }

    @Override
    public boolean deleteFilm(Long id) {
        try {
            if (!filmRepository.existsById(id)) {
                LOG_ERROR.error("[FILM][SERVICE][DELETE] not found id={}", id);
                throw new NotFoundException("Film not found");
            }
            filmRepository.deleteById(id);
            LOG_MAIN.info("[FILM][DB] deleted id={}", id);
            return true;
        } catch (NotFoundException nfe) {
            throw nfe;
        } catch (Exception ex) {
            LOG_ERROR.error("[FILM][SERVICE][DELETE] error id={}", id, ex);
            throw new RuntimeException("Unexpected error");
        }
    }
}
