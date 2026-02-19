package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.implementation.Sql2oTicketRepository;

import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oTicketRepositoryTest {
    private static Sql2oTicketRepository repository;

    @BeforeAll
    public static void initConnections() throws Exception {
        var properties = new Properties();
        try (var input = Sql2oTicketRepository.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(input);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var dataSource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(dataSource);

        repository = new Sql2oTicketRepository(sql2o);
    }

    @AfterEach
    public void clearTickets() {
        repository.clearTable();
    }

    @Test
    void whenSaveThenGetSameTicket() {
        var ticketToSave = new Ticket(1, 1, 1, 1, 1);
        var optional = repository.save(ticketToSave);

        assertThat(optional.get()).usingRecursiveAssertion().isEqualTo(ticketToSave);
    }

    @Test
    void whenSaveSameTicketTwiceThenGetEmptyOptional() {
        var ticketToSave = new Ticket(1, 1, 1, 1, 1);
        repository.save(ticketToSave);
        var optional = repository.save(ticketToSave);

        assertThat(optional).isEmpty();
    }
}