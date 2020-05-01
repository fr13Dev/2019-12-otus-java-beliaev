package ru.otus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.domain.User;
import ru.otus.repository.UserRepository;

import java.util.Collections;

@Controller
public class UserController {
    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping({"/", "users"})
    public String usersView(Model model) {
        var users = repository.findAll().orElse(Collections.emptyList());
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping({"new"})
    public String newUserView(Model model) {
        model.addAttribute("user", new User());
        return "new";
    }

    @PostMapping({"new"})
    public RedirectView userNew(@ModelAttribute(name = "user") User user) {
        repository.insert(user);
        return new RedirectView("/users", true);
    }
}
