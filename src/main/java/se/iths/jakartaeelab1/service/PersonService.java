package se.iths.jakartaeelab1.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import se.iths.jakartaeelab1.dto.PersonDto;
import se.iths.jakartaeelab1.dto.Persons;
import se.iths.jakartaeelab1.entity.Person;
import se.iths.jakartaeelab1.repository.PersonRepository;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PersonService {
    PersonRepository personRepository;

    @Inject
    public PersonService(PersonRepository personRepository){
        this.personRepository = personRepository;
    }

    public Persons allPersons(){
        return new Persons(personRepository.readAllPerson().stream().map(PersonDto::map).toList());
    }
}
