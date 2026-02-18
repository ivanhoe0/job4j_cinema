package ru.job4j.cinema.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.TicketService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FilmSessionControllerTest {
    private FilmSessionService filmSessionService;

    private TicketService ticketService;

    private FilmSessionController controller;

    @BeforeEach
    public void initServices() {
        filmSessionService = mock(FilmSessionService.class);
        ticketService = mock(TicketService.class);
        controller = new FilmSessionController(filmSessionService, ticketService);
    }

    @Test
    void whenFindAllThenOk() {
        var filmSessionDto1 = new FilmSessionDto(1, "test", "testHall", LocalDateTime.now(), LocalDateTime.now().plusHours(2), 1, 1, 1, 1);
        var filmSessionDto2 = new FilmSessionDto(2, "test2", "testHall2", LocalDateTime.now(), LocalDateTime.now().plusHours(2), 2, 2, 2, 2);
        var expectedSessions = List.of(filmSessionDto1, filmSessionDto2);
        when(filmSessionService.findAll()).thenReturn(List.of(filmSessionDto1, filmSessionDto2));

        var model = new ConcurrentModel();
        var view = controller.getAll(model);
        var actualSessions = model.getAttribute("sessions");

        assertThat(view).isEqualTo("sessions/list");
        assertThat(actualSessions).usingRecursiveComparison().isEqualTo(expectedSessions);
    }

    @Test
    void whenFindByIdThenOk() {
        var expectedSessionDto = new FilmSessionDto(1, "test", "testHall", LocalDateTime.now(), LocalDateTime.now().plusHours(2), 1, 1, 1, 1);
        when(filmSessionService.findById(1)).thenReturn(Optional.of(expectedSessionDto));

        var model = new ConcurrentModel();
        var view = controller.getById(1, model);
        var actualSession = model.getAttribute("seance");

        assertThat(view).isEqualTo("sessions/buy");
        assertThat(actualSession).usingRecursiveComparison().isEqualTo(expectedSessionDto);
    }

    @Test
    void whenFindByIdThenError() {
        when(filmSessionService.findById(anyInt())).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = controller.getById(1, model);
        var message = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(message).isEqualTo("Сеанс с указанным идентификатором не найден");
    }

    @Test
    void whenSuccessfullyBuy() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(new User(1, "Test", "Test", "Test"));
        var expectedSessionDto = new FilmSessionDto(1, "test", "testHall", LocalDateTime.now(), LocalDateTime.now().plusHours(2), 1, 1, 1, 1);
        var expectedTicket = new Ticket(1, 1, 1, 1, 1);
        var ticketCaptor = ArgumentCaptor.forClass(Ticket.class);
        when(ticketService.save(ticketCaptor.capture())).thenReturn(Optional.of(expectedTicket));

        var model = new ConcurrentModel();
        var view = controller.buy(expectedTicket, expectedSessionDto, model, request);
        var actualTicket = ticketCaptor.getValue();
        var message = model.getAttribute("message");

        assertThat(view).isEqualTo("sessions/success");
        assertThat(actualTicket).usingRecursiveComparison().isEqualTo(expectedTicket);
        assertThat(message).isEqualTo("Ряд: " + expectedTicket.getRowNumber() + " Место: " + expectedTicket.getPlaceNumber());

    }

    @Test
    void whenUnsuccessfullyBuy() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(new User(1, "Test", "Test", "Test"));
        var expectedSessionDto = new FilmSessionDto(1, "test", "testHall", LocalDateTime.now(), LocalDateTime.now().plusHours(2), 1, 1, 1, 1);
        var expectedTicket = new Ticket(1, 1, 1, 1, 1);
        var ticketCaptor = ArgumentCaptor.forClass(Ticket.class);
        when(ticketService.save(ticketCaptor.capture())).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = controller.buy(expectedTicket, expectedSessionDto, model, request);
        var actualTicket = ticketCaptor.getValue();
        var message = model.getAttribute("message");

        assertThat(view).isEqualTo("sessions/failure");
        assertThat(actualTicket).usingRecursiveComparison().isEqualTo(expectedTicket);
        assertThat(message).isEqualTo("Не удалось приобрести билет на заданное место. Вероятно оно уже занято. Перейдите на страницу бронирования билетов и попробуйте снова.");
    }
}