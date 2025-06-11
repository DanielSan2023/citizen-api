package com.example.service;

import com.example.dto.CitizenDtoFull;
import com.example.dto.CitizenDtoSimple;
import com.example.dto.CitizenRequestDto;
import com.example.dto.DocumentDtoRequest;
import com.example.exception.DuplicateDocumentTypeException;
import com.example.generator.IdGenerator;
import com.example.mapper.CitizenMapper;
import com.example.model.Citizen;
import com.example.model.Document;
import com.example.model.DocumentStatus;
import com.example.validator.CitizenValidator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.utility.CitizenConstants.YEARS_TO_ADD;

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
    public void assignDocument(String birthNumber, DocumentDtoRequest docDto) {
        Citizen citizen = getCitizenIfExist(birthNumber);

        validateNoDuplicateDocumentType(docDto, birthNumber);

        Document doc = citizenMapper.dtoToDocument(docDto);

        String generatedNumber = IdGenerator.generateNumberByDocumentType(docDto.getType());
        doc.setNumber(generatedNumber);

        LocalDate today = LocalDate.now();
        doc.setDateOfIssue(today);
        doc.setExpiryDate(today.plusYears(YEARS_TO_ADD));
        doc.setStatus(DocumentStatus.VALID);

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
    public Optional<CitizenDtoFull> findCitizenByBirthNumberWithDocuments(String birthNumber) {
        try {
            Citizen citizen = getCitizenIfExist(birthNumber);

            return Optional.of(citizenMapper.citizenToDto(citizen));

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    private Citizen getCitizenIfExist(String birthNumber) {
        try {

            return em.createQuery(
                            "SELECT DISTINCT c FROM Citizen c LEFT JOIN FETCH c.documents WHERE c.birthNumber = :birthNumber",
                            Citizen.class)
                    .setParameter("birthNumber", birthNumber)
                    .getSingleResult();

        } catch (NoResultException e) {
            throw new IllegalArgumentException("Citizen with birth number " + birthNumber + " does not exist.");
        }
    }

    private void validateNoDuplicateDocumentType(DocumentDtoRequest docDto, String birthNumber) {
        Long count = em.createQuery(
                        "SELECT COUNT(d) FROM Document d WHERE d.citizen.birthNumber = :birthNumber AND d.type = :type", Long.class)
                .setParameter("birthNumber", birthNumber)
                .setParameter("type", docDto.getType())
                .getSingleResult();

        if (count > 0) {
            throw new DuplicateDocumentTypeException("Citizen already has a document of type: " + docDto.getType());
        }
    }
}