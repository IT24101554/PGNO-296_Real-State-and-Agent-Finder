package com.example.realestateagentfinder.model;

import lombok.Data;
import java.io.Serializable;

@Data
public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String email;
    private String password;
    private String contactNumber;
    private String userType;

    // Constructor, getters and setters are handled by Lombok
}
