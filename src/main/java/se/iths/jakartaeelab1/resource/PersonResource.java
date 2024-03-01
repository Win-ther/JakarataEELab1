package se.iths.jakartaeelab1.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import se.iths.jakartaeelab1.dto.Persons;
import se.iths.jakartaeelab1.service.PersonService;

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

}