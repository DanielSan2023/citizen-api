package com.example.mapper;

import com.example.dto.CitizenDtoFull;
import com.example.dto.CitizenDtoSimple;
import com.example.dto.CitizenRequestDto;
import com.example.dto.DocumentDto;
import com.example.model.Citizen;
import com.example.model.Document;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface CitizenMapper {
    Citizen dtoToCitizen(CitizenRequestDto citizen);
    CitizenDtoFull citizenToDto(Citizen citizen);

    DocumentDto toDocumentDto(Document document);

    CitizenDtoSimple citizenToDtoSimple(Citizen citizen);

    List<CitizenDtoSimple> citizensToDto(List<Citizen> citizens);
}
