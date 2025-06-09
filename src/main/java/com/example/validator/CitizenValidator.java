package com.example.validator;

import com.example.model.Citizen;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ValidationException;

import static com.example.utility.CitizenConstants.DUPLICATE_BIRTH_NUMBER_EXCEPTION;

@ApplicationScoped
public class CitizenValidator {

    @PersistenceContext(unitName = "citizenPU")
    EntityManager em;

    public void validateBirthNumberUniqueness(String birthNumber) {
        boolean exists = !em.createQuery(
                        "SELECT c FROM Citizen c WHERE c.birthNumber = :birthNumber", Citizen.class)
                .setParameter("birthNumber", birthNumber)
                .getResultList()
                .isEmpty();
        ;
        if (exists) {
            throw new ValidationException(DUPLICATE_BIRTH_NUMBER_EXCEPTION);
        }
    }


}


