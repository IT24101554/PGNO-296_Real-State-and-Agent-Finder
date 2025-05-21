package com.example.realestateagentfinder.service;

import com.example.realestateagentfinder.model.User;
import com.example.realestateagentfinder.model.Client;
import com.example.realestateagentfinder.model.Agent;
import com.example.realestateagentfinder.util.FileUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileStorageService {

    @Value("${app.users.file:users.dat}")
    private String usersFile;

    @Value("${app.file.storage.location:./file-storage}")
    private String fileStorageLocation;

    @PostConstruct
    public void init() {
        try {
            Path storagePath = Paths.get(fileStorageLocation);
            Files.createDirectories(storagePath);

            Path profilesPath = Paths.get(fileStorageLocation, "profiles");
            Files.createDirectories(profilesPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage locations", e);
        }
    }

    public void saveUser(User user) {
        List<User> users = getAllUsers();

        boolean userExists = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                users.set(i, user);
                userExists = true;
                break;
            }
        }

        if (!userExists) {
            users.add(user);
        }

        FileUtil.writeObjectToFile(users, usersFile);
    }

    public List<User> getAllUsers() {
        List<User> users = FileUtil.readObjectFromFile(usersFile);
        return users != null ? users : new ArrayList<>();
    }

    public User getUserByEmail(String email) {
        List<User> users = getAllUsers();
        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public User getUserById(String id) {
        List<User> users = getAllUsers();
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Agent> getAllAgents() {
        List<User> users = getAllUsers();
        return users.stream()
                .filter(user -> user instanceof Agent)
                .map(user -> (Agent) user)
                .collect(Collectors.toList());
    }

    public void deleteUser(String id) {
        List<User> users = getAllUsers();
        users.removeIf(user -> user.getId().equals(id));
        FileUtil.writeObjectToFile(users, usersFile);
    }

    public String saveFile(MultipartFile file, String directory, String prefix) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file");
            }

            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String newFilename = prefix + "_" + System.currentTimeMillis() + fileExtension;
            Path destinationFile = Paths.get(fileStorageLocation, directory, newFilename);

            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

            return newFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
}
