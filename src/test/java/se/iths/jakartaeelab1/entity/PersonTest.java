package se.iths.jakartaeelab1.entity;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.iths.jakartaeelab1.entity.Person;

import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
public class PersonTest {

    private Person person;

    @BeforeEach
    void  setUp() {person = new Person(); }
    @Test
    void testHashCodeEquals(){
        EqualsVerifier.simple().forClass(Person.class).suppress(Warning.IDENTICAL_COPY_FOR_VERSIONED_ENTITY).suppress(Warning.SURROGATE_KEY).verify();
    }
    @Test
    void testSetGetId() {
        UUID id = UUID.randomUUID();
        person.setId(id);

        assertEquals(id, person.getId());
    }
    @Test
    void testSetGetName() {
        person.setName("Bob Doe");

        assertEquals("Bob Doe", person.getName());
    }
    @Test
    void testSetGetAge() {
        person.setAge(42);

        assertEquals(42, person.getAge());
    }
    @Test
    void testSetGetProfession() {
        person.setProfession("Spy");

        assertEquals("Spy", person.getProfession());
    }
}