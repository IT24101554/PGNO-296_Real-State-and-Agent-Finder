package com.example.realestateagentfinder.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Admin extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<String> managedRegions = new ArrayList<>();
    private String adminLevel; // SUPER_ADMIN, REGIONAL_ADMIN
    private LocalDateTime lastLogin;

    public Admin() {
        this.setUserType("ADMIN");
        this.adminLevel = "REGIONAL_ADMIN"; // Default value
    }

    // Admin-specific methods (polymorphism example)
    public boolean canManageAllAgents() {
        return "SUPER_ADMIN".equals(this.adminLevel);
    }

    public boolean canManageRegion(String region) {
        return canManageAllAgents() || managedRegions.contains(region);
    }
}