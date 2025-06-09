package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.xml.bind.annotation.*;

import java.util.List;

@XmlRootElement(name = "citizen")
@XmlAccessorType(XmlAccessType.FIELD)
public class CitizenDtoFull {

    private Long id;

    private String firstName;

    private String lastName;

    private String birthNumber;

    @NotBlank
    @Pattern(regexp = "\\d{6}/?\\d{3,4}", message = "Birth number must by 6 number + 3 or 4")
    @XmlElementWrapper(name = "documents")
    @XmlElement(name = "document")
    private List<DocumentDto> documents;

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

    public List<DocumentDto> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentDto> documents) {
        this.documents = documents;
    }
}

