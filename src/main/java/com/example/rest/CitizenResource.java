package com.example.rest;

import com.example.dto.CitizenDtoFull;
import com.example.dto.CitizenDtoSimple;
import com.example.dto.CitizenRequestDto;
import com.example.model.Citizen;
import com.example.model.Document;
import com.example.service.CitizenService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/citizens")
@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
public class CitizenResource {

    @Inject
    CitizenService citizenService;

    @POST
    public Response create(@Valid CitizenRequestDto citizen) {
        citizenService.register(citizen);
        return Response.ok(citizen).build();
    }

    @GET
    public List<CitizenDtoSimple> getAll() {
        return citizenService.findAll();
    }

    @GET
    @Path("/{id}")
    public CitizenDtoFull getCitizenById(@PathParam("id") Long citizenId) {
        return citizenService.findByIdWithDocuments(citizenId);
    }

    @POST
    @Path("/{id}/documents")
    public Response assignDocument(@PathParam("id") Long citizenId, Document document) {
        citizenService.assignDocument(citizenId, document);
        return Response.ok().build();
    }
}