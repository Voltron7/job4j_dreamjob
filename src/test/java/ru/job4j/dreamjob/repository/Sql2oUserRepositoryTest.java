package ru.job4j.dreamjob.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.configuration.DatasourceConfiguration;
import ru.job4j.dreamjob.model.User;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oUserRepositoryTest {
    private static Sql2oUserRepository sql2oUserRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oVacancyRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    public void clearUsers() {
        var users = sql2oUserRepository.findAll();
        for (var user : users) {
            sql2oUserRepository.deleteById(user.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        var optionalUser = sql2oUserRepository.save(new User(0, "Voltron7@gmail.com", "Valeri", "777"));
        var expected = sql2oUserRepository.findByEmailAndPassword(optionalUser.get().getEmail(), optionalUser.get().getPassword());
        assertThat(optionalUser).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        var user1 = sql2oUserRepository.save(new User(0, "email1@gmail.com", "name1", "123"));
        var user2 = sql2oUserRepository.save(new User(0, "email2@gmail.com", "name2", "456"));
        var user3 = sql2oUserRepository.save(new User(0, "email3@gmail.com", "name3", "789"));
        var result = sql2oUserRepository.findAll();
        assertThat(result).isEqualTo(List.of(user1.get(), user2.get(), user3.get()));
    }

    @Test
    public void whenNotSavedThenNothingFound() {
        assertThat(sql2oUserRepository.findAll()).isEqualTo(emptyList());
        assertThat(sql2oUserRepository.findByEmailAndPassword("email@gmail.com", "777")).isEqualTo(empty());
    }

    @Test
    public void whenUserIsAlreadyExistsThenException() {
        User user = new User(1, "Val@gmail.com", "Val", "333");
        sql2oUserRepository.save(user);
        assertThat(sql2oUserRepository.save(user)).isEqualTo(Optional.empty());
    }
}