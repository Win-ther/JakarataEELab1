package se.iths.jakartaeelab1.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import se.iths.jakartaeelab1.entity.Person;
import se.iths.jakartaeelab1.repository.PersonRepository;

import java.util.List;

@ApplicationScoped
public class PersonService {
    PersonRepository personRepository;

    @Inject
    public PersonService(PersonRepository personRepository){
        this.personRepository = personRepository;
    }

}
