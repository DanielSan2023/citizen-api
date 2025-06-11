package com.example.batch;

import jakarta.annotation.security.RunAs;
import jakarta.batch.operations.JobOperator;
import jakarta.batch.runtime.BatchRuntime;
import jakarta.ejb.Schedule;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;

import java.util.Properties;

@RunAs("batchAdmin")
@Stateless
public class BatchScheduler {

    @Inject
    private SecurityContext securityContext;

    @Schedule(hour = "2", minute = "0", persistent = false)
    public void runBatch() {
        System.out.println("Current user: " + securityContext.getCallerPrincipal());
        System.out.println("Is in batchAdmin role: " + securityContext.isCallerInRole("batchAdmin"));

        try {
            JobOperator jobOperator = BatchRuntime.getJobOperator();
            System.out.println("Starting batch job...");
            jobOperator.start("documentExpirationJob", new Properties());
        } catch (Exception e) {
            System.err.println("Failed to start batch job: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
