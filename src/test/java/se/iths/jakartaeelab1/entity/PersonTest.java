package se.iths.jakartaeelab1.entity;
import org.junit.jupiter.api.Test;
import se.iths.jakartaeelab1.entity.Person;

import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
public class PersonTest {

    @Test
    void testEquals() {
        UUID commonUUID = UUID.randomUUID();
        Person person1 = createPersonWithSameId("Bob Doe", 25, "Software Engineer", commonUUID);
        Person person2 = createPersonWithSameId("Jane Doe", 30, "Data Scientist", commonUUID);

        assertTrue(person1.equals(person2));
        assertTrue(person2.equals(person1));
    }
    @Test
    void testHashCodeEqual() {
        UUID commonUUID = UUID.randomUUID();
        Person person1 = createPersonWithSameId("Bob Doe", 25, "Software Engineer", commonUUID);
        Person person2 = createPersonWithSameId("Jane Doe", 30, "Data Scientist", commonUUID);

        assertEquals(person1.hashCode(), person2.hashCode(), "Hash codes should be equal");
    }

    @Test
    void testNotEquals() {
        Person person1 = new Person();
        person1.setId(UUID.randomUUID());
        person1.setName("Bob Doe");
        person1.setAge(25);
        person1.setProfession("Software Engineer");

        Person person2 = new Person();
        person2.setId(UUID.randomUUID());
        person2.setName("Jane Doe");
        person2.setAge(30);
        person2.setProfession("Data Scientist");

        assertFalse(person1.equals(person2));
        assertFalse(person2.equals(person1));
    }

    @Test
    void testHashCodeNotEqual() {
        Person person1 = new Person();
        person1.setId(UUID.randomUUID());
        person1.setName("Bob Doe");
        person1.setAge(25);
        person1.setProfession("Software Engineer");

        Person person2 = new Person();
        person2.setId(UUID.randomUUID());
        person2.setName("Jane Doe");
        person2.setAge(30);
        person2.setProfession("Data Scientist");

        assertNotEquals(person1.hashCode(), person2.hashCode());
    }

    private Person createPersonWithSameId(String name, int age, String profession, UUID id) {
        Person person = new Person();
        person.setId(id);
        person.setName(name);
        person.setAge(age);
        person.setProfession(profession);
        return person;
    }
}
