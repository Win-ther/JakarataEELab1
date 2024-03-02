package se.iths.jakartaeelab1.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.iths.jakartaeelab1.entity.Person;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
class PersonRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private PersonRepository personRepository;

    @Test
    void createPersonTest() {
        Person person = new Person();
        doNothing().when(entityManager).persist(any());

        personRepository.createPerson(person);

        verify(entityManager, times(1)).persist(person);
    }

    @Test
    void readAllPersonTest() {
        List<Person> personList = Arrays.asList(new Person(), new Person());

        jakarta.persistence.TypedQuery<Person> typedQueryMock = mock(jakarta.persistence.TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Person.class)))
                .thenReturn(typedQueryMock);

        when(typedQueryMock.getResultList())
                .thenReturn(personList);

        List<Person> result = personRepository.readAllPerson();

        assertNotNull(result);
        assertEquals(personList, result);
    }

    @Test
    void findPersonByIdTest() {
        UUID id = UUID.randomUUID();
        Person person = new Person();
        when(entityManager.find(eq(Person.class), eq(id))).thenReturn(person);

        Person result = personRepository.findPersonById(id);

        assertNotNull(result);
        assertEquals(person, result);
    }

    @Test
    void updatePersonTest() {
        Person updatedPerson = new Person();
        when(entityManager.merge(any())).thenReturn(updatedPerson);

        Person result = personRepository.updatePerson(updatedPerson);

        assertNotNull(result);
        assertEquals(updatedPerson, result);
        verify(entityManager, times(1)).merge(updatedPerson);
    }

    @Test
    void deletePersonTest() {
        UUID id = UUID.randomUUID();
        Person person = new Person();
        when(entityManager.find(eq(Person.class), eq(id))).thenReturn(person);

        personRepository.deletePerson(id);

        verify(entityManager, times(1)).remove(person);
    }
}