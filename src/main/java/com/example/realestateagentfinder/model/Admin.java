package com.example.realestateagentfinder.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class Admin extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    public Admin() {
        this.setUserType("ADMIN");
    }
}
