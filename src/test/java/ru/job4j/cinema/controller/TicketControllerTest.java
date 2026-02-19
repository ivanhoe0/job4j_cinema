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
import ru.job4j.cinema.service.TicketService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TicketControllerTest {

    private TicketService ticketService;

    private TicketController controller;

    @BeforeEach
    public void initServices() {
        ticketService = mock(TicketService.class);
        controller = new TicketController(ticketService);
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