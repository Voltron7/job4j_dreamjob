package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UserService;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {
    private UserService userService;
    private UserController userController;
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    public void initServices() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
        httpServletRequest = new MockHttpServletRequest();
    }

    @Test
    public void whenRequestRegistrationPage() {
        assertThat(userController.getRegistrationPage()).isEqualTo("users/register");
    }

    @Test
    public void whenRegisterUserThenGetIndexPage() {
        var user = new User(1, "email@gmail.com", "Valeri", "777");
        var userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        when(userService.save(userArgumentCaptor.capture())).thenReturn(Optional.of(user));

        var model = new ConcurrentModel();
        var view = userController.register(model, user);
        var actualUser = userArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/index");
        assertThat(actualUser).isEqualTo(user);
    }

    @Test
    public void whenRequestUsersLoginPage() {
        assertThat(userController.getLoginPage()).isEqualTo("users/login");
    }

    @Test
    public void whenRequestLoginUserAndCreateSessionThenGetIndexPage() {
        var user = new User(1, "email@gmail.com", "Valeri", "333");
        when(userService.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(Optional.of(user));

        var model = new ConcurrentModel();
        var view = userController.loginUser(user, model, httpServletRequest);

        assertThat(view).isEqualTo("redirect:/index");
    }

    @Test
    public void whenRequestLogoutThenGetUsersLoginPage() {
        assertThat(userController.logout(httpServletRequest.getSession())).isEqualTo("redirect:/users/login");
    }
}