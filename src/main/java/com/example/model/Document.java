package com.example.model;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import lombok.Getter;
import lombok.Setter;

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

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "citizen_id")
    @XmlTransient
    public Citizen citizen;

    //TODO field  - date for expiration, date of issue, etc.
}