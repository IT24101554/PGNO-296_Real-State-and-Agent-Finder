package com.example.realestateagentfinder.controller;

import com.example.realestateagentfinder.model.User;
import com.example.realestateagentfinder.model.Client;
import com.example.realestateagentfinder.model.Agent;
import com.example.realestateagentfinder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        User user = userService.login(email, password);
        if (user != null) {
            session.setAttribute("user", user);
            if (user instanceof Client) {
                return "redirect:/client/profile";
            } else if (user instanceof Agent) {
                return "redirect:/agent/profile";
            } else {
                return "redirect:/admin/dashboard";
            }
        }
        model.addAttribute("error", "Invalid credentials");
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("client", new Client());
        model.addAttribute("agent", new Agent());
        return "register";
    }

    @PostMapping("/register/client")
    public String registerClient(@ModelAttribute Client client, Model model) {
        try {
            userService.registerUser(client);
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("client", client);
            model.addAttribute("agent", new Agent());
            return "register";
        }
    }

    @PostMapping("/register/agent")
    public String registerAgent(
            @ModelAttribute Agent agent,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestParam(value = "serviceAreasInput", required = false) String serviceAreasInput,
            Model model) {
        try {
            // Process service areas from comma-separated string
            if (serviceAreasInput != null && !serviceAreasInput.trim().isEmpty()) {
                List<String> serviceAreas = Arrays.asList(serviceAreasInput.split("\\s*,\\s*"));
                agent.setServiceAreas(serviceAreas);
            }

            // Set default values
            agent.setVerified(false);
            agent.setAvailable(true);

            // Handle profile image if provided
            if (profileImage != null && !profileImage.isEmpty()) {
                // In a real application, you would save the file and set the path
                // For this example, we'll just set a placeholder
                agent.setProfilePicture("profile_" + System.currentTimeMillis() + ".jpg");
            }

            userService.registerUser(agent);
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("agent", agent);
            model.addAttribute("client", new Client());
            return "register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/client/profile")
    public String clientProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !(user instanceof Client)) {
            return "redirect:/login";
        }

        Client client = (Client) user;
        model.addAttribute("client", client);
        return "client-profile";
    }

    @PostMapping("/client/update")
    public String updateClientProfile(@ModelAttribute Client updatedClient, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || !(currentUser instanceof Client)) {
            return "redirect:/login";
        }

        Client client = (Client) currentUser;
        // Preserve the ID and user type
        updatedClient.setId(client.getId());
        updatedClient.setUserType("CLIENT");

        userService.updateUser(updatedClient);
        session.setAttribute("user", updatedClient);

        return "redirect:/client/profile?updated=true";
    }

    @PostMapping("/client/update-preferences")
    public String updateClientPreferences(@ModelAttribute Client updatedClient, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || !(currentUser instanceof Client)) {
            return "redirect:/login";
        }

        Client client = (Client) currentUser;
        // Update only the preferences while preserving other data
        client.setPreferredPropertyType(updatedClient.getPreferredPropertyType());
        client.setPreferredLocation(updatedClient.getPreferredLocation());

        userService.updateUser(client);
        session.setAttribute("user", client);

        return "redirect:/client/profile?updated=true";
    }
}
