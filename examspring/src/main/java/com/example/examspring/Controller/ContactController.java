package com.example.examspring.Controller;

import com.example.examspring.Dao.CategoryRepository;
import com.example.examspring.Entity.Category;
import com.example.examspring.Entity.Contact;
import com.example.examspring.Service.ContactService;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import com.example.examspring.Entity.User;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class ContactController {

    private final ContactService contactService;
    private final CategoryRepository categoryRepository;

    @GetMapping("/contacts")
    public String list(HttpSession session, Model model, @RequestParam(required = false) Long categoryId) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("categories", categoryRepository.findAll());
        if (categoryId != null && categoryId > 0 && user != null) {
            Optional<Category> category = categoryRepository.findById(categoryId);
            model.addAttribute("contacts", contactService.findByUserAndCategory(user, category));
        } else {
            List<Contact> contacts = contactService.findByUser(user);
            model.addAttribute("contacts", contacts);
        }
        return "contacts";
    }

    @GetMapping("/contacts/create")
    public String newContact(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("contact", new Contact());
        model.addAttribute("categories", categoryRepository.findAll());
        return "contact-form";
    }

    @PostMapping("/contacts/create")
    public String saveContact(@Valid @ModelAttribute Contact contact,
                              BindingResult result,
                              @RequestParam(required = false) Long categoryId,
                              HttpSession session,
                              Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            return "contact-form";
        }
        contact.setUser(user);
        if (categoryId != null) {
            contact.setCategory(categoryRepository.findById(categoryId).get());
        }
        contactService.save(contact);
        return "redirect:/contacts";
    }
//    Bug trouve (Methode pas proteger du tout)
//    @GetMapping("/contacts/delete/{id}")
//    public String deleteContact(@PathVariable Long id) {
//        contactService.deleteById(id);
//        return "redirect:/contacts";
//    }

    @GetMapping("/contacts/delete/{id}")
    public String deleteContact(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Contact contact = contactService.findById(id);
        if (contact == null) {
            return "redirect:/contacts";
        }
        contactService.deleteById(id);
        return "redirect:/contacts";
    }

    @GetMapping("/contacts/{id}")
    public String modifyContact(@PathVariable Long id, Model model) {
        Contact contact = contactService.findById(id);
        model.addAttribute("contact", contact);
        model.addAttribute("categories", categoryRepository.findAll());
        return "modify-contact-form";
    }

    @PostMapping("/contacts/edit/{id}")
    public String updateContact(@PathVariable Long id,
                                @Valid @ModelAttribute Contact contact,
                                BindingResult result,
                                @RequestParam(required = false) Long categoryId,
                                HttpSession session,
                                Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            return "contact-form";
        }
        contact.setId(id);
        contact.setUser(user);
        if (categoryId != null) {
            contact.setCategory(categoryRepository.findById(categoryId).get());
        }
        contactService.save(contact);
        return "redirect:/contacts";
    }
}