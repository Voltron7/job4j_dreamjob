package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import ru.job4j.dreamjob.dto.FileDto;
import ru.job4j.dreamjob.service.FileService;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FileControllerTest {
    private FileService fileService;
    private FileController fileController;
    private FileDto fileDto;

    @BeforeEach
    public void initServices() {
        fileDto = mock(FileDto.class);
        fileService = mock(FileService.class);
        fileController = new FileController(fileService);
    }

    @Test
    void whenGetFileByIdIsSuccessful() {
        when(fileService.getFileById(any(Integer.class))).thenReturn(Optional.of(fileDto));
        int fileId = 1;
        var responseEntity = fileController.getById(fileId);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void whenGetFileByIdIsUnsuccessful() {
        when(fileService.getFileById(any(Integer.class))).thenReturn(Optional.empty());
        int fileId = 1;
        var responseEntity = fileController.getById(fileId);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}