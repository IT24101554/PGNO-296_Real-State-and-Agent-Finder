package com.example.realestateagentfinder.controller;

import com.example.realestateagentfinder.model.Agent;
import com.example.realestateagentfinder.model.User;
import com.example.realestateagentfinder.model.Admin;
import com.example.realestateagentfinder.model.Appointment;
import com.example.realestateagentfinder.service.UserService;
import com.example.realestateagentfinder.service.AppointmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getUserType())) {
            return "redirect:/login";
        }

        // Update last login time
        if (user instanceof Admin) {
            Admin admin = (Admin) user;
            admin.setLastLogin(LocalDateTime.now());
            userService.updateUser(admin);
            session.setAttribute("user", admin);
        }

        // Get all agents for verification management
        List<Agent> agents = userService.getAllAgents();
        model.addAttribute("agents", agents);

        // Get statistics for the dashboard
        int totalAgents = agents.size();
        int verifiedAgents = (int) agents.stream().filter(Agent::isVerified).count();
        int availableAgents = (int) agents.stream().filter(a -> a.isVerified() && a.isAvailable()).count();

        model.addAttribute("totalAgents", totalAgents);
        model.addAttribute("verifiedAgents", verifiedAgents);
        model.addAttribute("availableAgents", availableAgents);

        // Get upcoming appointments for monitoring
        List<Appointment> upcomingAppointments = appointmentService.getUpcomingAppointments();
        model.addAttribute("upcomingAppointments", upcomingAppointments);

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

    // New Admin Management Methods

    @GetMapping("/agents")
    public String manageAgents(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getUserType())) {
            return "redirect:/login";
        }

        List<Agent> agents = userService.getAllAgents();
        model.addAttribute("agents", agents);

        // Group agents by specialization for better management
        Map<String, List<Agent>> agentsBySpecialization = agents.stream()
                .collect(Collectors.groupingBy(Agent::getSpecialization));

        model.addAttribute("agentsBySpecialization", agentsBySpecialization);

        return "admin-agents";
    }

    @GetMapping("/appointments")
    public String viewAppointments(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getUserType())) {
            return "redirect:/login";
        }
        List<Appointment> appointments = appointmentService.getAllAppointments();
        // Enrich appointments with client and agent names
        appointments.forEach(appointment -> {
            User client = userService.getUserById(appointment.getClientId());
            User agent = userService.getUserById(appointment.getAgentId());
            appointment.setClientName(client != null ? client.getName() : "Unknown");
            appointment.setAgentName(agent != null ? agent.getName() : "Unknown");
        });
        model.addAttribute("appointments", appointments);
        return "admin-appointments";
    }

    @PostMapping("/appointment/{id}/update-status")
    public String updateAppointmentStatus(
            @PathVariable String id,
            @RequestParam String status,
            HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getUserType())) {
            return "redirect:/login";
        }

        appointmentService.updateAppointmentStatus(id, status);
        return "redirect:/admin/appointments";
    }

    @GetMapping("/inactive-users")
    public String manageInactiveUsers(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getUserType())) {
            return "redirect:/login";
        }

        // Get inactive agents (those that haven't logged in for a while or updated their profile)
        List<Agent> inactiveAgents = userService.getInactiveAgents();
        model.addAttribute("inactiveAgents", inactiveAgents);

        return "admin-inactive-users";
    }

    @PostMapping("/batch-deactivate")
    public String batchDeactivateUsers(
            @RequestParam List<String> userIds,
            HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getUserType())) {
            return "redirect:/login";
        }

        for (String userId : userIds) {
            userService.deactivateUser(userId);
        }

        return "redirect:/admin/inactive-users?deactivated=true";
    }

    @GetMapping("/analytics")
    public String viewAnalytics(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getUserType())) {
            return "redirect:/login";
        }

        // Calculate statistics for analytics dashboard
        List<Agent> agents = userService.getAllAgents();
        List<Appointment> appointments = appointmentService.getAllAppointments();

        // Agent metrics
        int totalAgents = agents.size();
        int verifiedAgents = (int) agents.stream().filter(Agent::isVerified).count();
        double verificationRate = totalAgents > 0 ? (double) verifiedAgents / totalAgents * 100 : 0;

        // Appointment metrics
        int totalAppointments = appointments.size();
        int completedAppointments = (int) appointments.stream()
                .filter(a -> "COMPLETED".equals(a.getStatus())).count();
        int cancelledAppointments = (int) appointments.stream()
                .filter(a -> "CANCELLED".equals(a.getStatus())).count();

        // Most active regions
        Map<String, Long> appointmentsByLocation = appointments.stream()
                .collect(Collectors.groupingBy(Appointment::getLocation, Collectors.counting()));

        model.addAttribute("totalAgents", totalAgents);
        model.addAttribute("verifiedAgents", verifiedAgents);
        model.addAttribute("verificationRate", verificationRate);
        model.addAttribute("totalAppointments", totalAppointments);
        model.addAttribute("completedAppointments", completedAppointments);
        model.addAttribute("cancelledAppointments", cancelledAppointments);
        model.addAttribute("appointmentsByLocation", appointmentsByLocation);

        return "admin-analytics";
    }
}