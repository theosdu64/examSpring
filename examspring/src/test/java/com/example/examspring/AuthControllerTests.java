package com.example.examspring;

import com.example.examspring.Controller.AuthController;
import com.example.examspring.Dao.CategoryRepository;
import com.example.examspring.Dao.UserRepository;
import com.example.examspring.Entity.User;
import com.example.examspring.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthControllerTests {

    @Mock
    private UserService userService;
    @InjectMocks
    private AuthController authController;

    @Test
    @DisplayName("Should return register if sesult has error")
    void testShouldReturnRegisterIfResultHasError() {
        Model model = mock(Model.class);
        BindingResult  bindingResult = mock(BindingResult.class);
        User user = new User();

        when(bindingResult.hasErrors()).thenReturn(true);

        String result = authController.create(model,user,bindingResult);
        assertEquals("register",result);
    }

    @Test
    @DisplayName("Should return register if user already exists")
    void testShouldReturnRegisterIfUserAlreadyExists() {
        Model model = mock(Model.class);
        BindingResult bindingResult = mock(BindingResult.class);
        User user = new User();
        user.setEmail("test@test.com");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.existsByEmail("test@test.com")).thenReturn(true);

        String result = authController.create(model, user, bindingResult);

        assertEquals("register", result);
    }

    @Test
    @DisplayName("Should save user and return redirect to login")
    void testShouldSaveUserAndReturnRedirectToLogin() {
        Model model = mock(Model.class);
        BindingResult bindingResult = mock(BindingResult.class);
        User user = new User();

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.existsByEmail(any())).thenReturn(false);

        String result = authController.create(model, user, bindingResult);

        assertEquals("redirect:/login", result);
        verify(userService).save(user);
    }

    @Test
    @DisplayName("Should return login when credentials are wrong")
    void testShouldReturnLoginWhenCredentialsAreWrong() {
        HttpSession session = mock(HttpSession.class);
        Model model = mock(Model.class);

        when(userService.login("test@test.com", "wrongpassword")).thenReturn(null);

        String result = authController.login("test@test.com", "wrongpassword", session, model);

        assertEquals("login", result);
        verify(session, never()).setAttribute(any(), any());
    }

    @Test
    @DisplayName("Should redirect to contacts when login is successful")
    void testShouldRedirectToContactsWhenLoginSuccessful() {
        HttpSession session = mock(HttpSession.class);
        Model model = mock(Model.class);
        User user = new User();

        when(userService.login("test@test.com", "password")).thenReturn(user);

        String result = authController.login("test@test.com", "password", session, model);

        assertEquals("redirect:/contacts", result);
        verify(session).setAttribute("user", user);
    }

    @Test
    @DisplayName("Should redirect to home when logout")
    void testShouldRedirectToHomeWhenLogout() {
        HttpSession session = mock(HttpSession.class);
        String result = authController.logout(session);

        assertEquals("redirect:/", result);
        verify(session).invalidate();
    }
}
