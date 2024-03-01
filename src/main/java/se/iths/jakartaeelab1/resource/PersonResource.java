package se.iths.jakartaeelab1.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import se.iths.jakartaeelab1.dto.PersonDto;
import se.iths.jakartaeelab1.dto.Persons;
import se.iths.jakartaeelab1.entity.Person;
import se.iths.jakartaeelab1.service.PersonService;

import java.net.URI;
import java.util.UUID;

@Path("/persons")
public class PersonResource {

    @Context
    UriInfo uriInfo;

    private PersonService personService;

    public PersonResource() {
    }

    @Inject
    public PersonResource(PersonService personService) {
        this.personService = personService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Persons getAllPersons() {
        return personService.allPersons();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{id}")
    public PersonDto getPersonById(@PathParam("id") UUID id) {
        return personService.onePerson(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPerson(@Valid PersonDto personDto) {
        var person = personService.addPerson(personDto);
        return Response.created(
                URI.create(uriInfo.getAbsolutePath().toString() + "/" + person.getId())).build();
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Person updatePerson(@PathParam("id") UUID id, @Valid PersonDto personDto) {
        return personService.updatePerson(id, personDto);
    }

}