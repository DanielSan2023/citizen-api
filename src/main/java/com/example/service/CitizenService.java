package com.example.service;

import com.example.dto.CitizenDtoFull;
import com.example.dto.CitizenDtoSimple;
import com.example.dto.CitizenRequestDto;
import com.example.model.Document;

import java.util.List;

public interface CitizenService {
    public void register(CitizenRequestDto citizen);

    public void assignDocument(Long citizenId, Document doc);

    public List<CitizenDtoSimple> findAll();

    public CitizenDtoFull findByIdWithDocuments(Long citizenId);
}
