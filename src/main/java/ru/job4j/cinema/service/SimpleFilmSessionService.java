package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.FilmSessionRepository;
import ru.job4j.cinema.repository.HallRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class SimpleFilmSessionService implements FilmSessionService {

    private final FilmRepository filmRepository;

    private final HallRepository hallRepository;

    private final FilmSessionRepository filmSessionRepository;

    public SimpleFilmSessionService(FilmRepository filmRepository, HallRepository hallRepository, FilmSessionRepository filmSessionRepository) {
        this.filmRepository = filmRepository;
        this.hallRepository = hallRepository;
        this.filmSessionRepository = filmSessionRepository;
    }

    @Override
    public Collection<FilmSessionDto> findAll() {
        return filmSessionRepository.findAll().stream()
                .map(this::makeFromSession)
                .toList();
    }

    @Override
    public Optional<FilmSessionDto> findById(int id) {
        var sessionOptional = filmSessionRepository.findById(id);
        return sessionOptional.map(this::makeFromSession);
    }

    private FilmSessionDto makeFromSession(FilmSession session) {
        var filmOptional = filmRepository.findById(session.getFilmId());
        var hallOptional = hallRepository.findById(session.getHallId());
        if (filmOptional.isEmpty() || hallOptional.isEmpty()) {
            return null;
        }
        return new FilmSessionDto(
                session.getId(),
                filmOptional.get().getName(),
                hallOptional.get().getName(),
                session.getStartTime(),
                session.getEndTime(),
                hallOptional.get().getRowCount(),
                hallOptional.get().getPlaceCount(),
                session.getPrice(),
                filmOptional.get().getFileId()
        );
    }
}
