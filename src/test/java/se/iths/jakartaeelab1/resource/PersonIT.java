package se.iths.jakartaeelab1.resource;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import se.iths.jakartaeelab1.dto.PersonDto;
import se.iths.jakartaeelab1.dto.Persons;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonIT {

    @Container
    public static ComposeContainer testEnvironment = new ComposeContainer(new File("src/test/resources/compose-test.yml"))
            .withExposedService("db", 3306)
            .withExposedService("wildfly", 8080, Wait.forHttp("/jakartaeelabb/persons")
                    .forStatusCode(200))
            .withLocalCompose(true);
    static String host;
    static int port;

    @BeforeAll
    static void beforeAll() {
        host = testEnvironment.getServiceHost("wildfly", 8080);
        port = testEnvironment.getServicePort("wildfly", 8080);
    }

    @BeforeEach
    void before() {
        RestAssured.baseURI = "http://" + host + "/jakartaeelabb";
        RestAssured.port = port;
    }

    @Test
    @Order(1)
    @DisplayName("getAllPersonsShouldReturnEmptyListOfPersons")
    void getAllPersonsShouldReturnEmptyListOfPersons() {
        Persons persons = RestAssured.get("/persons").then()
                .statusCode(200)
                .extract()
                .as(Persons.class);
        assertEquals(List.of(), persons.persons());
    }

    @Test
    @Order(2)
    @DisplayName("addPersonShouldReturnListWithSamePerson")
    void addPersonShouldReturnListWithSamePerson() {
        String jsonString = "{\"name\" : \"Alf\",\"age\" : \"25\", \"profession\" : \"Kriminell\"}";
        RequestSpecification request = RestAssured.given();
        request.contentType(ContentType.JSON);
        request.baseUri("http://localhost:" + port + "/jakartaeelabb/persons");
        request.body(jsonString);

        Response response = request.post();
        ValidatableResponse validatableResponse = response.then();

        validatableResponse.statusCode(201);
        Persons persons = RestAssured.get("/persons").then()
                .statusCode(200)
                .extract()
                .as(Persons.class);
        assertEquals(List.of(new PersonDto("Alf", 25, "Kriminell")), persons.persons());
    }

    @Test
    @DisplayName("getPersonByIdShouldReturnPersonWithThatId")
    void getPersonByIdShouldReturnPersonWithThatId() {
        String jsonString = "{\"name\" : \"Sten\",\"age\" : \"204\", \"profession\" : \"Landowner\"}";
        RequestSpecification request = RestAssured.given();
        request.contentType(ContentType.JSON);
        request.baseUri("http://localhost:" + port + "/jakartaeelabb/persons");
        request.body(jsonString);

        Response response = request.post();
        String responseHeader = response.getHeader("Location");
        String[] splitHeader = responseHeader.split("/");
        UUID id = UUID.fromString((splitHeader[splitHeader.length - 1]));

        PersonDto personDto = RestAssured.get("/persons/" + id).then()
                .statusCode(200)
                .extract()
                .as(PersonDto.class);

        assertEquals(new PersonDto("Sten", 204, "Landowner"), personDto);
    }
}