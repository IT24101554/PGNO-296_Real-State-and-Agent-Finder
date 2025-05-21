package com.example.realestateagentfinder.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class Client extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String preferredPropertyType;
    private String preferredLocation;

    public Client() {
        this.setUserType("CLIENT");
    }
}
