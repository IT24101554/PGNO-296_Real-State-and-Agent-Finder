package com.example.realestateagentfinder.controller;

import com.example.realestateagentfinder.model.Agent;
import com.example.realestateagentfinder.model.User;
import com.example.realestateagentfinder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getUserType())) {
            return "redirect:/login";
        }

        List<Agent> agents = userService.getAllAgents();
        model.addAttribute("agents", agents);

        return "admin-dashboard";
    }

    @PostMapping("/verify-agent/{id}")
    public String verifyAgent(@PathVariable String id, @RequestParam boolean verified, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getUserType())) {
            return "redirect:/login";
        }

        userService.verifyAgent(id, verified);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/deactivate-user/{id}")
    public String deactivateUser(@PathVariable String id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getUserType())) {
            return "redirect:/login";
        }

        userService.deactivateUser(id);
        return "redirect:/admin/dashboard";
    }
}
