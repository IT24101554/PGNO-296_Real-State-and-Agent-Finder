package com.example.realestateagentfinder.controller;

import com.example.realestateagentfinder.model.Agent;
import com.example.realestateagentfinder.model.User;
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
@RequestMapping("/agent")
public class AgentController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !(user instanceof Agent)) {
            return "redirect:/login";
        }

        Agent agent = (Agent) user;
        model.addAttribute("agent", agent);
        return "agent-profile";
    }

    @PostMapping("/update")
    public String updateProfile(
            @ModelAttribute Agent updatedAgent,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestParam(value = "serviceAreasInput", required = false) String serviceAreasInput,
            @RequestParam(value = "newPassword", required = false) String newPassword,
            HttpSession session) {

        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || !(currentUser instanceof Agent)) {
            return "redirect:/login";
        }

        Agent agent = (Agent) currentUser;

        // Preserve ID, user type, and verification status
        updatedAgent.setId(agent.getId());
        updatedAgent.setUserType("AGENT");
        updatedAgent.setVerified(agent.isVerified());

        // Process service areas from comma-separated string
        if (serviceAreasInput != null && !serviceAreasInput.trim().isEmpty()) {
            List<String> serviceAreas = Arrays.asList(serviceAreasInput.split("\\s*,\\s*"));
            updatedAgent.setServiceAreas(serviceAreas);
        }

        // Handle profile image if provided
        if (profileImage != null && !profileImage.isEmpty()) {
            // In a real application, you would save the file and set the path
            // For this example, we'll just set a placeholder
            updatedAgent.setProfilePicture("profile_" + System.currentTimeMillis() + ".jpg");
        } else {
            // Keep existing profile picture
            updatedAgent.setProfilePicture(agent.getProfilePicture());
        }

        // Handle password update if provided
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            updatedAgent.setPassword(newPassword);
        } else {
            // Keep existing password
            updatedAgent.setPassword(agent.getPassword());
        }

        userService.updateUser(updatedAgent);
        session.setAttribute("user", updatedAgent);

        return "redirect:/agent/profile?updated=true";
    }

    @PostMapping("/toggle-availability")
    public String toggleAvailability(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !(user instanceof Agent)) {
            return "redirect:/login";
        }

        Agent agent = (Agent) user;
        agent.setAvailable(!agent.isAvailable());

        userService.updateUser(agent);
        session.setAttribute("user", agent);

        return "redirect:/agent/profile";
    }
}
