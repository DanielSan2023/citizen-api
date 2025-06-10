package com.example.service;

import com.example.dto.CitizenDtoFull;
import com.example.dto.CitizenDtoSimple;
import com.example.dto.CitizenRequestDto;
import com.example.dto.DocumentDtoRequest;
import com.example.model.Document;

import java.util.List;
import java.util.Optional;

public interface CitizenService {
    public void register(CitizenRequestDto citizen);

    public void assignDocument(Long citizenId, DocumentDtoRequest doc);

    public List<CitizenDtoSimple> findAllCitizens();

    public CitizenDtoFull findByIdWithDocuments(Long citizenId);

    public Optional<CitizenDtoFull> findByBirthNumberWithDocuments(String birthNumber);
}
