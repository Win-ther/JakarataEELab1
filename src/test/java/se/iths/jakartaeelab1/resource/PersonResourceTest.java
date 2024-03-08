package se.iths.jakartaeelab1.resource;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.spi.Dispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import se.iths.jakartaeelab1.dto.PersonDto;
import se.iths.jakartaeelab1.dto.Persons;
import se.iths.jakartaeelab1.entity.Person;
import se.iths.jakartaeelab1.service.PersonService;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonResourceTest {

    @Mock
    PersonService personService;

    Dispatcher dispatcher;

    @BeforeEach
    void setup() {
        dispatcher = MockDispatcherFactory.createDispatcher();
        var resource = new PersonResource(personService);
        dispatcher.getRegistry().addSingletonResource(resource);
    }

    @Test
    @DisplayName("get all persons as a list with GET should return status 200")
    void getAllPersonsReturnsWithStatus200() throws Exception {
        when(personService.allPersons()).thenReturn(new Persons(List.of()));

        MockHttpRequest request = MockHttpRequest.get("/persons");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        assertEquals(200, response.getStatus());
        assertEquals("{\"persons\":[]}", response.getContentAsString());
    }

    @Test
    @DisplayName("finding one person by id with GET should return status 200")
    void findingOnePersonByIdShouldReturnWithStatus200() throws Exception {
        UUID id = UUID.randomUUID();
        PersonDto personDto = new PersonDto("Peter", 49, "Doctor");

        when(personService.onePerson(id)).thenReturn(personDto);

        MockHttpRequest request = MockHttpRequest.get("/persons/" + id);
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        assertEquals(200, response.getStatus());
        assertEquals("{\"name\":\"Peter\",\"age\":49,\"profession\":\"Doctor\"}", response.getContentAsString());
    }

    @Test
    @DisplayName("not finding a person by id with GET should return status 404")
    void notFindingAPersonByIdShouldReturnWithStatus404() throws Exception {
        UUID id = UUID.randomUUID();

        when(personService.onePerson(id)).thenThrow(new NotFoundException());

        MockHttpRequest request = MockHttpRequest.get("/persons/" + id);
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        assertEquals(404, response.getStatus());
    }

    @Test
    @DisplayName("create new person with POST should return status 201")
    void createNewPersonPostShouldReturnWithStatus201() throws Exception {
        when(personService.addPerson(Mockito.any())).thenReturn(new Person());

        MockHttpRequest request = MockHttpRequest.post("/persons");
        request.contentType(MediaType.APPLICATION_JSON);
        request.content("{\"name\":\"Eleonore\",\"age\":33,\"profession\":\"Personal trainer\"}".getBytes());
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        assertEquals(201, response.getStatus());
    }

}