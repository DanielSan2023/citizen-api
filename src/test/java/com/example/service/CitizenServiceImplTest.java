package com.example.service;

import com.example.dto.CitizenDtoFull;
import com.example.dto.CitizenDtoSimple;
import com.example.dto.CitizenRequestDto;
import com.example.mapper.CitizenMapper;
import com.example.model.Citizen;
import com.example.model.Document;
import com.example.validator.CitizenValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CitizenServiceImplTest {

    CitizenServiceImpl service;
    EntityManager emMock;
    CitizenMapper mapperMock;
    CitizenValidator validatorMock;

    @BeforeEach
    void setUp() {
        service = new CitizenServiceImpl();
        emMock = mock(EntityManager.class);
        mapperMock = mock(CitizenMapper.class);
        validatorMock = mock(CitizenValidator.class);

        service.em = emMock;
        service.citizenMapper = mapperMock;
        service.citizenValidator = validatorMock;
    }

    @Test
    void GIVEN_valid_request_WHEN_register_THEN_persist_citizen() {
        // GIVEN
        CitizenRequestDto dto = new CitizenRequestDto();
        dto.setBirthNumber("1234567890");
        Citizen entity = new Citizen();

        when(mapperMock.dtoToCitizen(dto)).thenReturn(entity);

        // WHEN
        service.register(dto);

        // THEN
        verify(validatorMock).validateBirthNumberUniqueness("1234567890");
        verify(emMock).persist(entity);
    }

    @Test
    void GIVEN_valid_citizenId_and_document_WHEN_assignDocument_THEN_document_is_assigned_and_persisted() {
        // GIVEN
        Long citizenId = 1L;
        Citizen citizen = new Citizen();
        Document document = new Document();

        when(emMock.find(Citizen.class, citizenId)).thenReturn(citizen);

        // WHEN
        service.assignDocument(citizenId, document);

        // THEN
        assertEquals(citizen, document.getCitizen());
        verify(emMock).persist(document);
    }

    @Test
    void GIVEN_citizens_in_db_WHEN_findAllCitizens_THEN_return_mapped_dto_list() {
        // GIVEN
        List<Citizen> entities = List.of(new Citizen(), new Citizen());
        List<CitizenDtoSimple> dtos = List.of(new CitizenDtoSimple(), new CitizenDtoSimple());
        TypedQuery<Citizen> queryMock = mock(TypedQuery.class);

        when(emMock.createQuery(anyString(), eq(Citizen.class))).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(entities);
        when(mapperMock.citizensToDto(entities)).thenReturn(dtos);

        // WHEN
        List<CitizenDtoSimple> result = service.findAllCitizens();

        // THEN
        assertEquals(dtos, result);
    }

    @Test
    void GIVEN_existing_citizenId_WHEN_findByIdWithDocuments_THEN_return_mapped_dto() {
        // GIVEN
        Long id = 42L;
        Citizen entity = new Citizen();
        CitizenDtoFull dto = new CitizenDtoFull();
        TypedQuery<Citizen> queryMock = mock(TypedQuery.class);

        when(emMock.createQuery(anyString(), eq(Citizen.class))).thenReturn(queryMock);
        when(queryMock.setParameter("id", id)).thenReturn(queryMock);
        when(queryMock.getSingleResult()).thenReturn(entity);
        when(mapperMock.citizenToDto(entity)).thenReturn(dto);

        // WHEN
        CitizenDtoFull result = service.findByIdWithDocuments(id);

        // THEN
        assertEquals(dto, result);
    }

    @Test
    void GIVEN_existing_birthNumber_WHEN_findByBirthNumberWithDocuments_THEN_return_optional_dto() {
        // GIVEN
        String birthNumber = "1234567890";
        Citizen citizen = new Citizen();
        CitizenDtoFull dto = new CitizenDtoFull();
        TypedQuery<Citizen> queryMock = mock(TypedQuery.class);

        when(emMock.createQuery(anyString(), eq(Citizen.class))).thenReturn(queryMock);
        when(queryMock.setParameter("birthNumber", birthNumber)).thenReturn(queryMock);
        when(queryMock.getSingleResult()).thenReturn(citizen);
        when(mapperMock.citizenToDto(citizen)).thenReturn(dto);

        // WHEN
        Optional<CitizenDtoFull> result = service.findByBirthNumberWithDocuments(birthNumber);

        // THEN
        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
    }

    @Test
    void GIVEN_nonexistent_birthNumber_WHEN_findByBirthNumberWithDocuments_THEN_return_empty_optional() {
        // GIVEN
        String birthNumber = "9999999999";
        TypedQuery<Citizen> queryMock = mock(TypedQuery.class);

        when(emMock.createQuery(anyString(), eq(Citizen.class))).thenReturn(queryMock);
        when(queryMock.setParameter("birthNumber", birthNumber)).thenReturn(queryMock);
        when(queryMock.getSingleResult()).thenThrow(NoResultException.class);

        // WHEN
        Optional<CitizenDtoFull> result = service.findByBirthNumberWithDocuments(birthNumber);

        // THEN
        assertTrue(result.isEmpty());
    }
}
