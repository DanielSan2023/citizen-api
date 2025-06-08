package com.example.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Citizen {

    @XmlElement
    public String firstName;

    @XmlElement
    public String lastName;

    @XmlElement
    public String birthNumber;

    public Citizen() {
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthNumber() {
        return birthNumber;
    }
}