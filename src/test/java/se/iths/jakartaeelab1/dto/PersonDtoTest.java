package se.iths.jakartaeelab1.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.iths.jakartaeelab1.entity.Person;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonDtoTest {
    private static Validator validator;
    private Person person;
    private PersonDto personDto;
    @BeforeEach
    void setUpPerson() {
        person = new Person();
        person.setName("Bob Doe");
        person.setAge(42);
        person.setProfession("Spy");

        personDto = new PersonDto(
                "Bob Doe",
                42,
                "Spy"
        );
    }
    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory();
        validator = factory.getValidator();
    }
    @Test
    void validPersonDto() {
        PersonDto validPerson = new PersonDto("Bob Doe", 42, "Spy");

        var violations = validator.validate(validPerson);

        assertEquals(0, violations.size());
    }
    @Test
    void invalidNamePersonDto() {
        PersonDto invalidPerson = new PersonDto("", 42, "Spy");

        var violations = validator.validate(invalidPerson);

        assertEquals(1, violations.size());
        assertEquals("must not be empty", violations.iterator().next().getMessage());
    }
    @Test
    void invalidAgePersonDto() {
        PersonDto invalidPerson = new PersonDto("Bob Doe", -5, "Spy");

        var violations = validator.validate(invalidPerson);

        assertEquals(1, violations.size());
        assertEquals("must be greater than 0", violations.iterator().next().getMessage());
    }
    @Test
    void invalidProfessionPersonDto() {
        PersonDto invalidPerson = new PersonDto("Bob Doe", 42, "");

        var violations = validator.validate(invalidPerson);
        assertEquals(1, violations.size());
        assertEquals("must not be empty", violations.iterator().next().getMessage());
    }
    @Test
    void dtoContainsEntityFieldsAfterMapping() {
        PersonDto personDto = PersonDto.map(person);
        assertEquals("Bob Doe", personDto.name());
        assertEquals(42, personDto.age());
        assertEquals("Spy", personDto.profession());
    }
    @Test
    void entityContainsDtoFieldsAfterMapping() {
        Person person = PersonDto.map(personDto);
        assertEquals("Bob Doe", person.getName());
        assertEquals(42, person.getAge());
        assertEquals("Spy", person.getProfession());
    }
    @Test
    void mappingDtoShouldReturnEntity() {
        var person = PersonDto.map(personDto);
        assertEquals(person.getClass(), Person.class);
    }
    @Test
    void mappingEntityShouldReturnDto() {
        var personDto = PersonDto.map(person);
        assertEquals(personDto.getClass(), PersonDto.class);

    }
}


