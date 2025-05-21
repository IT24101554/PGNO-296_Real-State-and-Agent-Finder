package com.example.realestateagentfinder.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Agent extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String licenseNumber;
    private String specialization;
    private int yearsOfExperience;
    private List<String> serviceAreas = new ArrayList<>();
    private String profilePicture;
    private boolean isVerified;
    private boolean isAvailable;

    public Agent() {
        this.setUserType("AGENT");
        this.isVerified = false;
        this.isAvailable = true;
    }
}
