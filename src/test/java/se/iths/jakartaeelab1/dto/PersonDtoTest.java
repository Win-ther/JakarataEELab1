package se.iths.jakartaeelab1.dto;

import org.junit.jupiter.api.Test;
import se.iths.jakartaeelab1.dto.PersonDto;
import se.iths.jakartaeelab1.entity.Person;

import static org.junit.jupiter.api.Assertions.*;


public class PersonDtoTest {
    @Test
    void map_shouldMapPersonToPersonDto() {
        Person person = new Person();
        person.setName("John");
        person.setAge(25);
        person.setProfession("Developer");

        PersonDto personDto = PersonDto.map(person);

        assertEquals("John", person.getName());
        assertEquals(25, person.getAge());
        assertEquals("Developer", person.getProfession());
    }

    @Test
    void map_shouldMapPersonDtoToPerson() {
        PersonDto personDto = new PersonDto("Jane", 30, "Engineer");

        Person person = PersonDto.map(personDto);

        assertEquals("Jane", person.getName());
        assertEquals(30, person.getAge());
        assertEquals("Engineer", person.getProfession());
    }
}