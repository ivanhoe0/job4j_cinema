package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.GenreRepository;

import java.util.Collection;

@Service
public class SimpleFilmService implements FilmService {

    private final FilmRepository filmRepository;

    private final GenreRepository genreRepository;

    public SimpleFilmService(FilmRepository filmRepository, GenreRepository genreRepository) {
        this.filmRepository = filmRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public Collection<FilmDto> findAll() {
        return filmRepository.findAll().stream().map(this::makeFromFilm).toList();
    }

    private FilmDto makeFromFilm(Film film) {
        return new FilmDto(film.getId(),
                film.getName(),
                film.getDescription(),
                film.getYear(),
                genreRepository.findById(film.getGenreId()).getName(),
                film.getMinimalAge(),
                film.getDurationInMinutes(),
                film.getFileId());
    }
}
