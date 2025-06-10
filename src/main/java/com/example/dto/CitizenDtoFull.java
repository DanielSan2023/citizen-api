package com.example.dto;

import jakarta.xml.bind.annotation.*;

import java.util.List;

@XmlRootElement(name = "citizen")
@XmlAccessorType(XmlAccessType.FIELD)
public class CitizenDtoFull {

    private Long id;

    private String firstName;

    private String lastName;

    private String birthNumber;

    @XmlElement(name = "document")
    private List<DocumentDtoResponse> documents;

    public CitizenDtoFull() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthNumber() {
        return birthNumber;
    }

    public void setBirthNumber(String birthNumber) {
        this.birthNumber = birthNumber;
    }

    public List<DocumentDtoResponse> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentDtoResponse> documents) {
        this.documents = documents;
    }
}

