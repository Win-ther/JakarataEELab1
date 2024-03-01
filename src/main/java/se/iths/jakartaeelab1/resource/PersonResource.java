package se.iths.jakartaeelab1.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import se.iths.jakartaeelab1.dto.PersonDto;
import se.iths.jakartaeelab1.dto.Persons;
import se.iths.jakartaeelab1.service.PersonService;

import java.net.URI;
import java.util.UUID;

@Path("/persons")
public class PersonResource {

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
        return Response.created(URI.create("http://localhost:8080/labb1/api/persons/" + person.getId())).build();
    }

}