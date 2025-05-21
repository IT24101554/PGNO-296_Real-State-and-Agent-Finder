package com.example.realestateagentfinder.service;

import com.example.realestateagentfinder.model.Appointment;
import com.example.realestateagentfinder.model.Agent;
import com.example.realestateagentfinder.model.Client;
import com.example.realestateagentfinder.util.FileUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Value("${app.appointments.file:appointments.dat}")
    private String appointmentsFile;

    // CRUD Operations for Appointments

    // Create a new appointment
    public Appointment createAppointment(Appointment appointment) {
        List<Appointment> appointments = getAllAppointments();

        // Generate ID for new appointment
        appointment.setId(UUID.randomUUID().toString());
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setUpdatedAt(LocalDateTime.now());

        // Set default status if not provided
        if (appointment.getStatus() == null) {
            appointment.setStatus("SCHEDULED");
        }

        appointments.add(appointment);
        saveAppointments(appointments);

        return appointment;
    }

    // Read all appointments
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = FileUtil.readObjectFromFile(appointmentsFile);
        return appointments != null ? appointments : new ArrayList<>();
    }

    // Get appointment by ID
    public Appointment getAppointmentById(String id) {
        List<Appointment> appointments = getAllAppointments();
        return appointments.stream()
                .filter(appointment -> appointment.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Update an appointment
    public Appointment updateAppointment(Appointment appointment) {
        List<Appointment> appointments = getAllAppointments();

        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getId().equals(appointment.getId())) {
                // Preserve creation date
                LocalDateTime createdAt = appointments.get(i).getCreatedAt();
                appointment.setCreatedAt(createdAt);
                appointment.setUpdatedAt(LocalDateTime.now());

                appointments.set(i, appointment);
                saveAppointments(appointments);
                return appointment;
            }
        }

        throw new RuntimeException("Appointment not found");
    }

    // Delete an appointment
    public void deleteAppointment(String id) {
        List<Appointment> appointments = getAllAppointments();

        boolean removed = appointments.removeIf(appointment -> appointment.getId().equals(id));

        if (removed) {
            saveAppointments(appointments);
        } else {
            throw new RuntimeException("Appointment not found");
        }
    }

    // Additional Methods for Admin Management

    // Get appointments by agent ID
    public List<Appointment> getAppointmentsByAgentId(String agentId) {
        List<Appointment> appointments = getAllAppointments();
        return appointments.stream()
                .filter(appointment -> appointment.getAgentId().equals(agentId))
                .collect(Collectors.toList());
    }

    // Get appointments by client ID
    public List<Appointment> getAppointmentsByClientId(String clientId) {
        List<Appointment> appointments = getAllAppointments();
        return appointments.stream()
                .filter(appointment -> appointment.getClientId().equals(clientId))
                .collect(Collectors.toList());
    }

    // Get appointments by status
    public List<Appointment> getAppointmentsByStatus(String status) {
        List<Appointment> appointments = getAllAppointments();
        return appointments.stream()
                .filter(appointment -> appointment.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    // Get upcoming appointments (for monitoring by admin)
    public List<Appointment> getUpcomingAppointments() {
        List<Appointment> appointments = getAllAppointments();
        LocalDateTime now = LocalDateTime.now();

        return appointments.stream()
                .filter(appointment -> appointment.getAppointmentTime().isAfter(now))
                .filter(appointment -> "SCHEDULED".equals(appointment.getStatus()))
                .sorted((a1, a2) -> a1.getAppointmentTime().compareTo(a2.getAppointmentTime()))
                .collect(Collectors.toList());
    }

    // Change appointment status (for admin management)
    public Appointment updateAppointmentStatus(String id, String status) {
        Appointment appointment = getAppointmentById(id);
        if (appointment == null) {
            throw new RuntimeException("Appointment not found");
        }

        appointment.setStatus(status);
        appointment.setUpdatedAt(LocalDateTime.now());

        return updateAppointment(appointment);
    }

    // Save appointments list to file
    private void saveAppointments(List<Appointment> appointments) {
        FileUtil.writeObjectToFile(appointments, appointmentsFile);
    }


}