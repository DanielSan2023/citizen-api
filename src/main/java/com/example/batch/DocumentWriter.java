package com.example.batch;

import com.example.model.Document;
import com.example.model.DocumentStatus;
import com.example.service.EmailService;
import jakarta.batch.api.chunk.AbstractItemWriter;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDate;
import java.util.List;

@Dependent
@Named("documentWriter")
public class DocumentWriter extends AbstractItemWriter {

    @Inject
    EmailService emailService;

    @PersistenceContext(unitName = "citizenPU")
    EntityManager em;

    @Override
    public void writeItems(List<Object> items) {
        LocalDate now = LocalDate.now();

        for (Object item : items) {
            Document doc = (Document) item;

            if (doc.getExpiryDate() == null) {
                doc.setStatus(DocumentStatus.VALID);
            } else if (doc.getExpiryDate().isBefore(now)) {
                doc.setStatus(DocumentStatus.EXPIRED);
            } else if (!doc.getExpiryDate().isAfter(now.plusDays(30))) {
                doc.setStatus(DocumentStatus.EXPIRING_SOON);
            } else {
                doc.setStatus(DocumentStatus.VALID);
            }

            em.merge(doc);
            emailService.sendExpirationEmail(doc);
        }
    }
}
