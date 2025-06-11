package com.example.service;

import com.example.dto.CitizenDtoFull;
import com.example.dto.CitizenDtoSimple;
import com.example.dto.CitizenRequestDto;
import com.example.dto.DocumentDtoRequest;
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
    void GIVEN_valid_birthNumber_and_document_WHEN_assignDocument_THEN_document_is_assigned_and_persisted() {
        // GIVEN
        String birthNumber = "1234567890";
        Citizen citizen = new Citizen();
        DocumentDtoRequest documentDto = new DocumentDtoRequest();
        documentDto.setType(/* nejaký typ, napr. */ com.example.model.DocumentType.PASSPORT);

        TypedQuery<Citizen> citizenQueryMock = mock(TypedQuery.class);
        TypedQuery<Long> countQueryMock = mock(TypedQuery.class);

        when(emMock.createQuery(contains("SELECT DISTINCT c"), eq(Citizen.class))).thenReturn(citizenQueryMock);
        when(citizenQueryMock.setParameter("birthNumber", birthNumber)).thenReturn(citizenQueryMock);
        when(citizenQueryMock.getSingleResult()).thenReturn(citizen);

        when(emMock.createQuery(contains("SELECT COUNT"), eq(Long.class))).thenReturn(countQueryMock);
        when(countQueryMock.setParameter("birthNumber", birthNumber)).thenReturn(countQueryMock);
        when(countQueryMock.setParameter("type", documentDto.getType())).thenReturn(countQueryMock);
        when(countQueryMock.getSingleResult()).thenReturn(0L);

        Document mockDocument = new Document();
        when(mapperMock.dtoToDocument(documentDto)).thenReturn(mockDocument);

        // WHEN
        service.assignDocument(birthNumber, documentDto);

        // THEN
        assertEquals(citizen, mockDocument.getCitizen());
        verify(emMock).persist(mockDocument);
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
        Optional<CitizenDtoFull> result = service.findCitizenByBirthNumberWithDocuments(birthNumber);

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
        Optional<CitizenDtoFull> result = service.findCitizenByBirthNumberWithDocuments(birthNumber);

        // THEN
        assertTrue(result.isEmpty());
    }

    @Test
    void GIVEN_duplicate_documentType_WHEN_assignDocument_THEN_throw_DuplicateDocumentTypeException() {
        // GIVEN
        String birthNumber = "1234567890";
        DocumentDtoRequest documentDto = new DocumentDtoRequest();
        documentDto.setType(com.example.model.DocumentType.PASSPORT);

        Citizen citizen = new Citizen();
        TypedQuery<Citizen> citizenQueryMock = mock(TypedQuery.class);
        TypedQuery<Long> countQueryMock = mock(TypedQuery.class);

        when(emMock.createQuery(contains("SELECT DISTINCT c"), eq(Citizen.class))).thenReturn(citizenQueryMock);
        when(citizenQueryMock.setParameter("birthNumber", birthNumber)).thenReturn(citizenQueryMock);
        when(citizenQueryMock.getSingleResult()).thenReturn(citizen);

        when(emMock.createQuery(contains("SELECT COUNT"), eq(Long.class))).thenReturn(countQueryMock);
        when(countQueryMock.setParameter("birthNumber", birthNumber)).thenReturn(countQueryMock);
        when(countQueryMock.setParameter("type", documentDto.getType())).thenReturn(countQueryMock);
        when(countQueryMock.getSingleResult()).thenReturn(1L); // Duplicate!

        // THEN
        org.junit.jupiter.api.Assertions.assertThrows(
                com.example.exception.DuplicateDocumentTypeException.class,
                () -> service.assignDocument(birthNumber, documentDto)
        );
    }
}
