package com.example.realestateagentfinder.model;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Appointment implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String clientId;
    private String agentId;
    private LocalDateTime appointmentTime;
    private String location;
    private String status; // SCHEDULED, COMPLETED, CANCELLED
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String clientName;
    private String agentName;

    // Getters and setters
    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }
    public String getAgentName() { return agentName; }
    public void setAgentName(String agentName) { this.agentName = agentName; }
}