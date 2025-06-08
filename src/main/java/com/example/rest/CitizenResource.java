package com.example.rest;

import com.example.model.Citizen;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/citizens")
public class CitizenResource {

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public Response createCitizen(Citizen citizen) {
        System.out.println("Citizen received: " + citizen.getFirstName());
        return Response.ok("Citizen " + citizen.getFirstName() + " saved").build();
    }
}