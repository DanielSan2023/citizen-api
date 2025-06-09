package com.example.rest;

import com.example.dto.CitizenDtoFull;
import com.example.dto.CitizenDtoSimple;
import com.example.dto.CitizenRequestDto;
import com.example.model.Document;
import com.example.service.CitizenService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;

import java.util.List;
import java.util.Optional;

@Path("/citizens")
@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
public class CitizenResource {

    @Inject
    CitizenService citizenService;

    @Operation(summary = "Create a new citizen",
            description = "Registers a new citizen in the system based on the provided request body.")
    @POST
    public Response create(@Valid CitizenRequestDto citizen) {
        citizenService.register(citizen);
        return Response.ok(citizen).build();
    }

    @Operation(summary = "Get all citizens",
            description = "Retrieves a list of all citizens registered in the system.")
    @GET
    public List<CitizenDtoSimple> getAllCitizens() {
        return citizenService.findAllCitizens();
    }

    @Operation(summary = "Get a citizen by ID",
            description = "Fetches detailed information about a specific citizen by their ID.")
    @GET
    @Path("/{id}")
    public CitizenDtoFull getCitizenById(@PathParam("id") Long citizenId) {
        return citizenService.findByIdWithDocuments(citizenId);
    }

    @Operation(summary = "Get a citizen by birth number",
            description = "Fetches detailed information about a specific citizen by their birth number.")
    @GET
    @Path("/birth/{number}")
    public Response getCitizenByBirthNumber(@PathParam("number") String birthNumber) {
        return citizenService.findByBirthNumberWithDocuments(birthNumber)
                .map(Response::ok)
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND)).build();
    }

    @Operation(summary = "Create a document for a citizen",
            description = "Assigns a document to a citizen based on the provided ID and document details.")
    @POST
    @Path("/{id}/documents")
    public Response assignDocument(@PathParam("id") Long citizenId, Document document) {
        citizenService.assignDocument(citizenId, document);
        return Response.ok().build();
    }
}