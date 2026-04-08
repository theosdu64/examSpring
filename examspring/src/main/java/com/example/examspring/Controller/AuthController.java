package com.example.examspring.Controller;

import com.example.examspring.Entity.User;
import com.example.examspring.Service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public String create(Model model, @Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }
        Boolean exist = userService.existsByEmail(user.getEmail());
        if (exist) {
            model.addAttribute("message", "User already exists");
            return "register";
        }
        userService.save(user);
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(String email, String password, HttpSession session, Model model) {
        User loginUser = userService.login(email, password);
        if (loginUser != null) {
            session.setAttribute("user", loginUser);
            return "redirect:/contacts";
        }
        model.addAttribute("message", "Wrong email or password");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }


}
