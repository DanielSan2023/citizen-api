package com.example.service;

import com.example.dto.CitizenDtoFull;
import com.example.dto.CitizenDtoSimple;
import com.example.dto.CitizenRequestDto;
import com.example.mapper.CitizenMapper;
import com.example.model.Citizen;
import com.example.model.Document;
import com.example.validator.CitizenValidator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CitizenServiceImpl implements CitizenService {

    @PersistenceContext(unitName = "citizenPU")
    EntityManager em;

    @Inject
    CitizenMapper citizenMapper;

    @Inject
    CitizenValidator citizenValidator;

    @Transactional
    public void register(CitizenRequestDto citizen) {
        citizenValidator.validateBirthNumberUniqueness(citizen.getBirthNumber());

        Citizen citizenEntity = citizenMapper.dtoToCitizen(citizen);
        em.persist(citizenEntity);
    }

    @Transactional
    public void assignDocument(Long citizenId, Document doc) {
        Citizen citizen = em.find(Citizen.class, citizenId);
        doc.setCitizen(citizen);
        em.persist(doc);
    }

    public List<CitizenDtoSimple> findAllCitizens() {
        List<Citizen> citizens = em.createQuery("SELECT c FROM Citizen c", Citizen.class).getResultList();
        return citizenMapper.citizensToDto(citizens);
    }

    @Override
    public CitizenDtoFull findByIdWithDocuments(Long citizenId) {
        Citizen citizen = em.createQuery("SELECT c FROM Citizen c LEFT JOIN FETCH c.documents WHERE c.id = :id", Citizen.class)
                .setParameter("id", citizenId)
                .getSingleResult();
        return citizenMapper.citizenToDto(citizen);
    }

    @Override
    public Optional<CitizenDtoFull> findByBirthNumberWithDocuments(String birthNumber) {
            try {
                Citizen citizen = em.createQuery(
                                "SELECT c FROM Citizen c LEFT JOIN FETCH c.documents WHERE c.birthNumber = :birthNumber",
                                Citizen.class)
                        .setParameter("birthNumber", birthNumber)
                        .getSingleResult();

                return Optional.of(citizenMapper.citizenToDto(citizen));
            } catch (NoResultException e) {
                return Optional.empty();
            }
    }
}