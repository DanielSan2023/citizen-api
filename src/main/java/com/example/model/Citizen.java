package com.example.model;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "citizens")
@XmlRootElement
public class Citizen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    @XmlElement
    public String firstName;

    @Column(nullable = false)
    @XmlElement
    public String lastName;

    @Column(nullable = false, unique = true)
    @XmlElement
    public String birthNumber;

    @OneToMany(mappedBy = "citizen", cascade = CascadeType.ALL)
    @XmlElementWrapper(name = "documents")
    @XmlElement(name = "document")
    public List<Document> documents = new ArrayList<>();

    public Citizen() {
    }

    public Long getId() {
        return id;
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

    public List<Document> getDocuments() {
        return documents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Citizen citizen = (Citizen) o;
        return birthNumber != null && birthNumber.equals(citizen.birthNumber);
    }

    @Override
    public int hashCode() {
        return birthNumber != null ? birthNumber.hashCode() : 0;
    }

}