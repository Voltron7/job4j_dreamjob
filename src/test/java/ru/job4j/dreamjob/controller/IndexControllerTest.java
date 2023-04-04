package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class IndexControllerTest {
    private final IndexController indexController = new IndexController();

    @Test
    public void whenRequestIndexPageThenGetIndexPage() {
        assertThat(indexController.getIndex()).isEqualTo("index");
    }
}