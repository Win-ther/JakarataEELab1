package se.iths.jakartaeelab1.dto;

import jakarta.validation.*;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import se.iths.jakartaeelab1.entity.Person;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;




public class PersonDtoTest {
    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void map_shouldMapPersonToPersonDto() {
        Person person = new Person();
        person.setName("John");
        person.setAge(25);
        person.setProfession("Developer");

        PersonDto personDto = PersonDto.map(person);

        assertEquals("John", personDto.name());
        assertEquals(25, personDto.age());
        assertEquals("Developer", personDto.profession());

        PersonDto mappedPersonDto = PersonDto.map(person);

        Set<ConstraintViolation<PersonDto>> violations = validator.validate(mappedPersonDto);

        assertTrue(violations.isEmpty(), "Validation should pass for mapping Person to PersonDto");
    }

    @Test
    void map_shouldMapPersonDtoToPerson() {
        PersonDto personDto = new PersonDto(
                "Jane",
                30,
                "Engineer");

        Person person = PersonDto.map(personDto);

        assertEquals("Jane", person.getName());
        assertEquals(30, person.getAge());
        assertEquals("Engineer", person.getProfession());

        PersonDto personDtoInput = new PersonDto("Jane", 30, "Engineer");
        Set<ConstraintViolation<PersonDto>> violations = validator.validate(personDtoInput);

        assertTrue(violations.isEmpty(), "Validation should pass for mapping PersonDto to Person");

        Person mappedPerson = PersonDto.map(personDtoInput);

        assertEquals("Jane", mappedPerson.getName());
        assertEquals(30, mappedPerson.getAge());
        assertEquals("Engineer", mappedPerson.getProfession());
    }


}


