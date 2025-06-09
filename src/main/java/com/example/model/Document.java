package com.example.model;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

@Entity
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

    @ManyToOne
    @JoinColumn(name = "citizen_id")
    @XmlTransient
    public Citizen citizen;

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
    }
}