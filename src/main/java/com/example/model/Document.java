package com.example.model;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@XmlRootElement
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue
    public Long id;

    @XmlElement
    @Enumerated(EnumType.STRING)
    public DocumentType type;

    @XmlElement
    public String number;

    @XmlElement
    public LocalDate dateOfIssue;

    @XmlElement
    public LocalDate expiryDate;

    @XmlElement
    @Enumerated(EnumType.STRING)
    public DocumentStatus status = DocumentStatus.VALID;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "citizen_id")
    @XmlTransient
    public Citizen citizen;
}