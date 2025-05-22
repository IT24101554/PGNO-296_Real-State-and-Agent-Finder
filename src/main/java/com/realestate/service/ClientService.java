package com.realestate.service;

import com.realestate.model.Client;
import com.realestate.util.ClientLinkedList;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ClientService {
    private static final String FILE_PATH = "clients.txt";
    private ClientLinkedList clients;

    public ClientService() {
        this.clients = new ClientLinkedList();
        loadFromFile();
    }

    public Client createClient(Client client) {
        if (client.getId() == null) {
            client.setId(generateNewId());
        }
        clients.add(client);
        saveToFile();
        return client;
    }

    public Client getClientById(Long id) {
        return clients.getById(id);
    }

    public List<Client> getAllClients() {
        return clients.toList();
    }

    public boolean updateClient(Client client) {
        boolean updated = clients.update(client);
        if (updated) {
            saveToFile();
        }
        return updated;
    }

    public boolean deleteClient(Long id) {
        boolean deleted = clients.remove(id);
        if (deleted) {
            saveToFile();
        }
        return deleted;
    }

    private Long generateNewId() {
        List<Client> clientList = clients.toList();
        Long maxId = 0L;
        for (Client client : clientList) {
            if (client.getId() > maxId) {
                maxId = client.getId();
            }
        }
        return maxId + 1;
    }

    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    Long id = Long.parseLong(parts[0]);
                    String name = parts[1];
                    String email = parts[2];
                    String phone = parts[3];
                    String preferredPropertyType = parts[4];
                    LocalDate registrationDate = LocalDate.parse(parts[5], formatter);
                    double budget = Double.parseDouble(parts[6]);
                    String notes = parts.length > 7 ? parts[7] : "";

                    Client client = new Client(id, name, email, phone,
                            preferredPropertyType, budget, notes);
                    client.setRegistrationDate(registrationDate);
                    clients.add(client);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Client client : clients.toList()) {
                writer.write(client.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}