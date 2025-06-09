package com.example.dto;

import com.example.model.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "document")
@XmlAccessorType(XmlAccessType.FIELD)
public class DocumentDto {
    private Long id;

    @NotNull(message = "Document type is required: ID_CARD, PASSPORT, or DRIVING_LICENSE")
    private DocumentType type;

    @NotBlank(message = "Document number is required")
    private String number;

    public DocumentDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentType getType() {
        return type;
    }

    public void setType(DocumentType type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
