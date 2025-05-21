package com.example.realestateagentfinder.service;

import com.example.realestateagentfinder.model.User;
import com.example.realestateagentfinder.model.Client;
import com.example.realestateagentfinder.model.Agent;
import com.example.realestateagentfinder.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private FileStorageService fileStorageService;

    @Value("${app.admin.email:admin@realestate.com}")
    private String adminEmail;

    @Value("${app.admin.password:admin123}")
    private String adminPassword;

    @Value("${app.agent.inactive.days:30}")
    private Integer agentInactiveDays;

    @PostConstruct
    public void init() {
        // Create admin user if it doesn't exist
        if (getUserByEmail(adminEmail) == null) {
            Admin admin = new Admin();
            admin.setId(UUID.randomUUID().toString());
            admin.setName("System Administrator");
            admin.setEmail(adminEmail);
            admin.setPassword(adminPassword);
            admin.setContactNumber("0000000000");
            admin.setAdminLevel("SUPER_ADMIN");  // Set as super admin
            admin.setLastLogin(LocalDateTime.now());
            fileStorageService.saveUser(admin);
        }
    }

    public User registerUser(User user) {
        // Check if email already exists
        if (getUserByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already registered");
        }

        // Generate ID for new user
        user.setId(UUID.randomUUID().toString());

        fileStorageService.saveUser(user);
        return user;
    }

    public User login(String email, String password) {
        User user = getUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public User getUserByEmail(String email) {
        return fileStorageService.getUserByEmail(email);
    }

    public User getUserById(String id) {
        return fileStorageService.getUserById(id);
    }

    public User updateUser(User user) {
        User existingUser = getUserById(user.getId());
        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }

        // If email is being changed, check if new email already exists
        if (!existingUser.getEmail().equals(user.getEmail()) && getUserByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already in use");
        }

        fileStorageService.saveUser(user);
        return user;
    }

    public List<Agent> getAllAgents() {
        return fileStorageService.getAllAgents();
    }

    public void verifyAgent(String agentId, boolean isVerified) {
        User user = getUserById(agentId);
        if (user instanceof Agent) {
            Agent agent = (Agent) user;
            agent.setVerified(isVerified);
            fileStorageService.saveUser(agent);
        } else {
            throw new RuntimeException("User is not an agent");
        }
    }

    public void deactivateUser(String userId) {
        fileStorageService.deleteUser(userId);
    }

    public String saveProfileImage(MultipartFile file, String userId) {
        return fileStorageService.saveFile(file, "profiles", userId);
    }

    // New methods for Admin Management

    /**
     * Get a list of inactive agents based on predefined criteria
     */
    public List<Agent> getInactiveAgents() {
        List<Agent> allAgents = getAllAgents();

        // Criteria: Agents who are verified but marked as unavailable
        return allAgents.stream()
                .filter(agent -> agent.isVerified() && !agent.isAvailable())
                .collect(Collectors.toList());
    }

    /**
     * Batch verify multiple agents at once
     */
    public void batchVerifyAgents(List<String> agentIds, boolean verified) {
        for (String agentId : agentIds) {
            try {
                verifyAgent(agentId, verified);
            } catch (Exception e) {
                // Log the error but continue processing the batch
                System.err.println("Error verifying agent " + agentId + ": " + e.getMessage());
            }
        }
    }

    /**
     * Get all users (for admin management)
     */
    public List<User> getAllUsers() {
        return fileStorageService.getAllUsers();
    }

    /**
     * Find agents by specialization (for admin filtering)
     */
    public List<Agent> findAgentsBySpecialization(String specialization) {
        List<Agent> allAgents = getAllAgents();

        return allAgents.stream()
                .filter(agent -> specialization.equals(agent.getSpecialization()))
                .collect(Collectors.toList());
    }

    /**
     * Find agents by service area (for admin regional management)
     */
    public List<Agent> findAgentsByServiceArea(String serviceArea) {
        List<Agent> allAgents = getAllAgents();

        return allAgents.stream()
                .filter(agent -> agent.getServiceAreas().contains(serviceArea))
                .collect(Collectors.toList());
    }

    /**
     * Count agents by verification status (for admin dashboard)
     */
    public int countAgentsByVerificationStatus(boolean verified) {
        List<Agent> agents = getAllAgents();

        return (int) agents.stream()
                .filter(agent -> agent.isVerified() == verified)
                .count();
    }



}