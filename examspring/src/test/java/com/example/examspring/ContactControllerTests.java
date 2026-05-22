package com.example.examspring;

import com.example.examspring.Controller.ContactController;
import com.example.examspring.Dao.CategoryRepository;
import com.example.examspring.Entity.Category;
import com.example.examspring.Entity.Contact;
import com.example.examspring.Entity.User;
import com.example.examspring.Service.ContactService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ContactControllerTests {

    @Mock
    private ContactService contactService;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ContactController contactController;

    // GetByID
    @Test
    @DisplayName("Should redirect to login if user is not connected when he search contact")
    void testshouldRedirectToLoginWhenNotConnected() {
        HttpSession session = mock(HttpSession.class);
        Model model = mock(Model.class);

        when(session.getAttribute("user")).thenReturn(null);

        String listContact = contactController.list(session, model, null);
        assertEquals("redirect:/login", listContact);
    }

    // GetByID
    @Test
    @DisplayName("Should return all contacts of a single user")
    void testshouldReturnAllContactsOfAUser() {
        HttpSession session = mock(HttpSession.class);
        Model model = mock(Model.class);
        User user = new User();

        when(session.getAttribute("user")).thenReturn(user);
        when(categoryRepository.findAll()).thenReturn(List.of());
        when(contactService.findByUser(user)).thenReturn(List.of(new Contact(), new Contact()));

        String listContact = contactController.list(session, model, null);
        assertEquals("contacts", listContact);
        verify(contactService).findByUser(user);
    }

    // Save
    @Test
    @DisplayName("should redirect to login when user is not in session")
    void shouldRedirectToLoginWhenSavingContact() {
        HttpSession session = mock(HttpSession.class);
        Model model = mock(Model.class);
        Contact contact = new Contact();

        when(session.getAttribute("user")).thenReturn(null);

        String result = contactController.saveContact(contact, null, null, session, model);
        assertEquals("redirect:/login", result);
    }

    // sAve
    @Test
    @DisplayName("should save contact and redirect to contacts")
    void shouldSaveContactAndRedirect() {
        HttpSession session = mock(HttpSession.class);
        Model model = mock(Model.class);
        Contact contact = new Contact();
        User user = new User();
        BindingResult bindingResult = mock(BindingResult.class);

        when(session.getAttribute("user")).thenReturn(user);
        when(bindingResult.hasErrors()).thenReturn(false);

        String result = contactController.saveContact(contact, bindingResult, null, session, model);

        assertEquals("redirect:/contacts", result);
        verify(contactService).save(contact);
    }

    // deletbyId
    @Test
    @DisplayName("Should redirect to login when user is not in session")
    void shouldRedirectToLoginWhenSavingContactAndRedirect() {
        HttpSession session = mock(HttpSession.class);

        when(session.getAttribute(null)).thenReturn(null);

        String result = contactController.deleteContact(1L, session);

        assertEquals("redirect:/login", result);
    }

    // DeleteById
    @Test
    @DisplayName("Should return redirect to contacts if contact doesn't exist")
    void shouldRedirectWhenContactDoesNotExist() {
        HttpSession session = mock(HttpSession.class);
        User user = new User();

        when(session.getAttribute("user")).thenReturn(user);
        when(contactService.findById(1L)).thenReturn(null);

        String result = contactController.deleteContact(1L, session);

        assertEquals("redirect:/contacts", result);
        verify(contactService, never()).deleteById(any());
    }

    // DeleteById
    @Test
    @DisplayName("Should delete contact and return redirect contacts")
    void shouldDeleteContactAndRedirect() {
        HttpSession session = mock(HttpSession.class);
        User user = new User();
        user.setId(1L);

        Contact contact = new Contact();
        contact.setUser(user);

        when(session.getAttribute("user")).thenReturn(user);
        when(contactService.findById(1L)).thenReturn(contact);

        String result = contactController.deleteContact(1L, session);

        assertEquals("redirect:/contacts", result);
        verify(contactService).deleteById(1L);
    }
}
