package se.iths.jakartaeelab1.service;

import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.iths.jakartaeelab1.dto.PersonDto;
import se.iths.jakartaeelab1.entity.Person;
import se.iths.jakartaeelab1.repository.PersonRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    private final UUID testId = UUID.randomUUID();
    private final Person testPerson = new Person();
    private final PersonDto testPersonDto = new PersonDto("Test Name", 25, "Test Profession");

    @BeforeEach
    void setUp() {
        testPerson.setId(testId);
        testPerson.setName("Test Name");
        testPerson.setAge(25);
        testPerson.setProfession("Test Profession");
    }

    @Test
    void testAddPerson() {
        when(personRepository.createPerson(any(Person.class))).thenReturn(testPerson);

        Person result = personService.addPerson(testPersonDto);

        assertEquals("Test Name", result.getName());
        assertEquals(25, result.getAge());
        assertEquals("Test Profession", result.getProfession());

        verify(personRepository, times(1)).createPerson(any(Person.class));
    }

    @Test
    void testAllPersons() {
        when(personRepository.readAllPerson()).thenReturn(List.of(testPerson));

        var result = personService.allPersons();


        assertEquals(1, result.persons().size());
        assertEquals("Test Name", result.persons().get(0).name());

        verify(personRepository).readAllPerson();
    }

    @Test
    void testOnePerson() {
        when(personRepository.findPersonById(testId)).thenReturn(testPerson);

        var result = personService.onePerson(testId);

        assertEquals("Test Name", result.name());

        verify(personRepository).findPersonById(testId);
    }

    @Test
    void testUpdatePerson() {
        when(personRepository.findPersonById(testId)).thenReturn(testPerson);
        when(personRepository.updatePerson(any(Person.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PersonDto updateDto = new PersonDto("Updated Name", 30, "Updated Profession");
        var result = personService.updatePerson(testId, updateDto);

        assertEquals("Updated Name", result.name());
        assertEquals(30, result.age());
        assertEquals("Updated Profession", result.profession());

        verify(personRepository).updatePerson(any(Person.class));
    }

    @Test
    void testRemovePerson() {
        when(personRepository.findPersonById(testId)).thenReturn(testPerson);
        doNothing().when(personRepository).deletePerson(testId);

        personService.removePerson(testId);

        verify(personRepository).deletePerson(testId);
    }

    @Test
    void testOnePersonNotFoundException() {
        UUID nonExistentId = UUID.randomUUID();
        assertThrows(NotFoundException.class, () -> personService.onePerson(nonExistentId));
        verify(personRepository).findPersonById(nonExistentId);
    }

    @Test
    void shouldInstantiateService() {
        PersonService service = new PersonService();
        assertNotNull(service);
    }
}
