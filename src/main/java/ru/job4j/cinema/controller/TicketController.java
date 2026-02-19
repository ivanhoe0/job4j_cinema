package ru.job4j.cinema.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.TicketService;

@Controller
@RequestMapping("/sessions")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
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
