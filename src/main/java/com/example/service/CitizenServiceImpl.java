package com.example.service;

import com.example.dto.CitizenDtoFull;
import com.example.dto.CitizenDtoSimple;
import com.example.dto.CitizenRequestDto;
import com.example.mapper.CitizenMapper;
import com.example.model.Citizen;
import com.example.model.Document;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class CitizenServiceImpl implements CitizenService {

    @PersistenceContext(unitName = "citizenPU")
    EntityManager em;

    @Inject
    CitizenMapper citizenMapper;

    @Transactional
    public void register(CitizenRequestDto citizen) {
        Citizen citizenEntity = citizenMapper.dtoToCitizen(citizen);
        em.persist(citizenEntity);
    }

    @Transactional
    public void assignDocument(Long citizenId, Document doc) {
        Citizen citizen = em.find(Citizen.class, citizenId);
        doc.setCitizen(citizen);
        em.persist(doc);
    }

    public List<CitizenDtoSimple> findAll() {
        List<Citizen>  citizens =  em.createQuery("SELECT c FROM Citizen c", Citizen.class).getResultList();
        return citizenMapper.citizensToDto(citizens);
    }

    @Override
    public CitizenDtoFull findByIdWithDocuments(Long citizenId) {
        Citizen citizen = em.createQuery("SELECT c FROM Citizen c LEFT JOIN FETCH c.documents WHERE c.id = :id", Citizen.class)
                .setParameter("id", citizenId)
                .getSingleResult();
        return citizenMapper.citizenToDto(citizen);
    }
}