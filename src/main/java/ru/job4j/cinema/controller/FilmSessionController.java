package ru.job4j.cinema.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.FilmService;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.TicketService;

@Controller
@RequestMapping("/sessions")
public class FilmSessionController {

    private final FilmSessionService filmSessionService;

    private final TicketService ticketService;

    public FilmSessionController(FilmSessionService filmSessionService, TicketService ticketService) {
        this.filmSessionService = filmSessionService;
        this.ticketService = ticketService;
    }

    @GetMapping()
    public String getAll(Model model) {
        model.addAttribute("sessions", filmSessionService.findAll());
        return "sessions/list";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable int id, Model model) {
        var optionalSession = filmSessionService.findById(id);
        if (optionalSession.isEmpty()) {
            model.addAttribute("message", "Сеанс с указанным идентификатором не найден");
            return "errors/404";
        }
        model.addAttribute("seance", optionalSession.get());
        return "sessions/buy";
    }

    @PostMapping("/buy")
    public String buy(@ModelAttribute Ticket ticket, @ModelAttribute FilmSessionDto session, Model model, HttpServletRequest request) {
        var user = (User) request.getSession().getAttribute("user");
        ticket.setUserId(user.getId());
        ticket.setSessionId(session.getId());
        var ticketOptional = ticketService.save(ticket);
        if (ticketOptional.isEmpty()) {
            model.addAttribute("message", "Не удалось приобрести билет на заданное место. Вероятно оно уже занято. Перейдите на страницу бронирования билетов и попробуйте снова.");
            return "sessions/failure";
        }
        model.addAttribute("message", "Ряд: " + ticket.getRowNumber() + " Место: " + ticket.getPlaceNumber());
        return "sessions/success";
    }
}
