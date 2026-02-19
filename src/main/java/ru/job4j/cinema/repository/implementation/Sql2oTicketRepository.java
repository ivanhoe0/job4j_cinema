package ru.job4j.cinema.repository.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.TicketRepository;

import java.sql.SQLException;
import java.util.Optional;

@Repository
public class Sql2oTicketRepository implements TicketRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(Sql2oTicketRepository.class);

    private final Sql2o sql2o;

    public Sql2oTicketRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Ticket> save(Ticket ticket) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("""
                    INSERT INTO tickets(session_id, row_number, place_number, user_id)
                    VALUES(:session_id, :row_number, :place_number, :user_id)
                    """);
            query.addParameter("session_id", ticket.getSessionId())
                    .addParameter("row_number", ticket.getRowNumber())
                    .addParameter("place_number", ticket.getPlaceNumber())
                    .addParameter("user_id", ticket.getUserId());
            try {
                int generatedId = query.executeUpdate().getKey(Integer.class);
                ticket.setId(generatedId);
                return Optional.of(ticket);
            } catch (Sql2oException e) {
                var cause = e.getCause();
                if (cause instanceof SQLException sqlEx) {
                    if ("23502".equals(sqlEx.getSQLState())) {
                        LOGGER.error("Это место уже забронировано");
                    } else {
                        LOGGER.error("SQLState: {}", sqlEx.getSQLState());
                    }
                }
                return Optional.empty();
            }
        }
    }

    public void clearTable() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("DELETE FROM tickets");
            query.executeUpdate();
        }
    }
}
