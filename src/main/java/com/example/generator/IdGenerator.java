package com.example.generator;

import com.example.model.DocumentType;

import java.util.UUID;

public class IdGenerator {
    public static String generateFromUuid(String prefix) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return prefix + uuid.substring(0, 7).toUpperCase();
    }

    public static String generateNumberByDocumentType(DocumentType type) {
        return switch (type) {
            case ID_CARD -> generateFromUuid("ID");
            case DRIVER_LICENSE -> generateFromUuid("DL");
            case PASSPORT -> generateFromUuid("PP");
            case RESIDENCE_PERMIT -> generateFromUuid("RP");
            default -> throw new IllegalArgumentException("Unknown document type: " + type);
        };
    }
}

