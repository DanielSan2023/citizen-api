package com.example.validator;

import com.example.model.Citizen;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static com.example.utility.CitizenConstants.DUPLICATE_BIRTH_NUMBER_EXCEPTION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CitizenValidatorTest {
    private CitizenValidator validator;
    private EntityManager emMock;
    private TypedQuery<Citizen> queryMock;

    @BeforeEach
    void setUp() {
        validator = new CitizenValidator();
        emMock = mock(EntityManager.class);
        queryMock = mock(TypedQuery.class);
        validator.em = emMock;
    }

    @Test
    void GIVEN_valid_number_WHEN_validateBirthNumberUniqueness_THEN_do_nothing() {
        // GIVEN
        String birthNumber = "1234567890";

        // WHEN
        when(emMock.createQuery(anyString(), eq(Citizen.class))).thenReturn(queryMock);
        when(queryMock.setParameter("birthNumber", birthNumber)).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(Collections.emptyList());

        // THEN
        assertDoesNotThrow(() -> validator.validateBirthNumberUniqueness(birthNumber));
    }

    @Test
    void GIVEN_exist_valid_number_WHEN_validateBirthNumberUniqueness_THEN_throw_exception() {
        // GIVEN
        String birthNumber = "1234567890";

        // WHEN
        when(emMock.createQuery(anyString(), eq(Citizen.class))).thenReturn(queryMock);
        when(queryMock.setParameter("birthNumber", birthNumber)).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(List.of(new Citizen()));

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validator.validateBirthNumberUniqueness(birthNumber)
        );

        // THEN
        assertEquals(DUPLICATE_BIRTH_NUMBER_EXCEPTION, exception.getMessage());
    }
}