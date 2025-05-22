package com.realestate.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Client implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String preferredPropertyType;
    private LocalDate registrationDate;
    private double budget;
    private String notes;

    public Client() {
        this.registrationDate = LocalDate.now();
    }

    public Client(Long id, String name, String email, String phone,
                  String preferredPropertyType, double budget, String notes) {
        this();
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.preferredPropertyType = preferredPropertyType;
        this.budget = budget;
        this.notes = notes;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPreferredPropertyType() { return preferredPropertyType; }
    public void setPreferredPropertyType(String preferredPropertyType) { this.preferredPropertyType = preferredPropertyType; }
    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }
    public double getBudget() { return budget; }
    public void setBudget(double budget) { this.budget = budget; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String toFileString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return id + "," + name + "," + email + "," + phone + "," +
                preferredPropertyType + "," + registrationDate.format(formatter) + "," +
                budget + "," + notes;
    }

    @Override
    public String toString() {
        return "Client: " + name + " (" + email + ")";
    }
}