package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Film;

import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oFilmRepositoryTest {
    private static Sql2oFilmRepository repository;

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

        repository = new Sql2oFilmRepository(sql2o);
    }

    @Test
    void whenFindByIdThenGetFilm() {
        var optional = repository.findById(1);
        var film = new Film(1, "The rock",
                "The Rock (1996) is a high-octane action thriller directed by Michael Bay, featuring Sean Connery as a former British spy and Nicolas Cage as an FBI chemist. They must infiltrate Alcatraz Island to stop a rogue general, played by Ed Harris, who threatens to launch deadly missiles at San Francisco.", 1996, 1, 12, 136, 1);
        assertThat(optional).isPresent();
        assertThat(optional.get()).usingRecursiveComparison().isEqualTo(film);
    }

    @Test
    void whenFindByIdThenGetEmptyOptional() {
        var optional = repository.findById(10);

        assertThat(optional).isEmpty();
    }

    @Test
    void whenFindAllThenGetListOfAllFilms() {
        var fetchedList = repository.findAll();
        var expectedList = List.of(
                new Film(1, "The rock",
                        "The Rock (1996) is a high-octane action thriller directed by Michael Bay, featuring Sean Connery as a former British spy and Nicolas Cage as an FBI chemist. They must infiltrate Alcatraz Island to stop a rogue general, played by Ed Harris, who threatens to launch deadly missiles at San Francisco.", 1996, 1, 12, 136, 1),
                new Film(2, "Titanic",
                        "James Cameron’s Titanic (1997) is an epic romance-disaster film starring Leonardo DiCaprio (Jack) and Kate Winslet (Rose), depicting the 1912 maiden voyage of the RMS Titanic. The story follows a high-society woman and a poor artist who fall in love before the ship strikes an iceberg.", 1997, 2, 12, 194, 2),
                new Film(3, "The ring",
                        "The Ring (2002) is a supernatural horror thriller directed by Gore Verbinski, starring Naomi Watts as journalist Rachel Keller. She investigates a cursed videotape that kills viewers seven days after watching it, following a phone call. After her son watches the tape, she must uncover the secrets of the ghost girl Samara to break the curse.", 2002, 3, 18, 115, 3)
        );

        assertThat(fetchedList).usingRecursiveComparison().isEqualTo(expectedList);
    }
}