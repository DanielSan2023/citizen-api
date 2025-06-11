package com.example.batch;

import com.example.model.Document;
import jakarta.batch.api.chunk.AbstractItemReader;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

@Dependent
@Named("documentReader")
public class DocumentReader extends AbstractItemReader {

    @PersistenceContext(unitName = "citizenPU")
    EntityManager em;

    private Iterator<Document> iterator;

    @Override
    public Object readItem() {
        if (iterator == null) {
            List<Document> docs = em.createQuery(
                            "SELECT d FROM Document d WHERE d.expiryDate <= :date", Document.class)
                    .setParameter("date", LocalDate.now().plusDays(30))
                    .getResultList();
            iterator = docs.iterator();
        }

        return iterator.hasNext() ? iterator.next() : null;
    }
}