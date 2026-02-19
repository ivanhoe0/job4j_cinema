package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.FilmSessionRepository;
import ru.job4j.cinema.repository.HallRepository;
import ru.job4j.cinema.service.implementation.SimpleFilmSessionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleFilmSessionServiceTest {
    private FilmRepository filmRepository;

    private HallRepository hallRepository;

    private FilmSessionRepository filmSessionRepository;

    private FilmSessionService sessionService;

    @BeforeEach
    public void initRepositories() {
        filmRepository = mock(FilmRepository.class);
        hallRepository = mock(HallRepository.class);
        filmSessionRepository = mock(FilmSessionRepository.class);
        sessionService = new SimpleFilmSessionService(filmRepository, hallRepository, filmSessionRepository);
    }

    @Test
    void whenFindAllThenOk() {
        var filmSessionDto1 = new FilmSessionDto(1, "test", "testHall", LocalDateTime.now(), LocalDateTime.now().plusHours(2), 1, 1, 1, 1);
        var filmSessionDto2 = new FilmSessionDto(2, "test2", "testHall2", LocalDateTime.now(), LocalDateTime.now().plusHours(2), 2, 2, 2, 2);
        var expectedSessions = List.of(filmSessionDto1, filmSessionDto2);
        var expectedFilm1 = new Film(1, "test", "test", 1, 1, 1, 1, 1);
        var expectedFilm2 = new Film(2, "test2", "test", 1, 1, 1, 1, 2);
        when(filmRepository.findById(1)).thenReturn(Optional.of(expectedFilm1));
        when(filmRepository.findById(2)).thenReturn(Optional.of(expectedFilm2));
        var expectedHall1 = new Hall(1, "testHall", 1, 1, "testDesc");
        var expectedHall2 = new Hall(2, "testHall2", 2, 2, "testDesc");
        when(hallRepository.findById(1)).thenReturn(Optional.of(expectedHall1));
        when(hallRepository.findById(2)).thenReturn(Optional.of(expectedHall2));
        var expectedSession1 = new FilmSession(1, 1, 1, filmSessionDto1.getStartDate(), filmSessionDto1.getEndDate(), filmSessionDto1.getPrice());
        var expectedSession2 = new FilmSession(2, 2, 2, filmSessionDto2.getStartDate(), filmSessionDto2.getEndDate(), filmSessionDto2.getPrice());
        when(filmSessionRepository.findById(1)).thenReturn(Optional.of(expectedSession1));
        when(filmSessionRepository.findById(2)).thenReturn(Optional.of(expectedSession2));
        when(filmSessionRepository.findAll()).thenReturn(List.of(expectedSession1, expectedSession2));

        var actualList = sessionService.findAll();

        assertThat(actualList).usingRecursiveComparison().isEqualTo(expectedSessions);
    }

    @Test
    void whenFindByIdThenOk() {
        var expectedSessionDto = new FilmSessionDto(1, "test", "testHall", LocalDateTime.now(), LocalDateTime.now().plusHours(2), 1, 1, 1, 1);
        var expectedFilm1 = new Film(1, "test", "test", 1, 1, 1, 1, 1);
        when(filmRepository.findById(1)).thenReturn(Optional.of(expectedFilm1));
        var expectedHall1 = new Hall(1, "testHall", 1, 1, "testDesc");
        when(hallRepository.findById(1)).thenReturn(Optional.of(expectedHall1));
        var expectedSession1 = new FilmSession(1, 1, 1, expectedSessionDto.getStartDate(), expectedSessionDto.getEndDate(), expectedSessionDto.getPrice());
        when(filmSessionRepository.findById(1)).thenReturn(Optional.of(expectedSession1));

        assertThat(sessionService.findById(1)).isPresent();
        assertThat(sessionService.findById(1).get()).usingRecursiveComparison().isEqualTo(expectedSessionDto);
    }
}