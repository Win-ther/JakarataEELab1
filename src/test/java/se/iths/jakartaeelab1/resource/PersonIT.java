package se.iths.jakartaeelab1.resource;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import jakarta.ws.rs.NotFoundException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import se.iths.jakartaeelab1.dto.PersonDto;
import se.iths.jakartaeelab1.dto.Persons;

import java.io.File;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;

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
    @DisplayName("getAllPersons should return empty list of persons")
    void getAllPersonsShouldReturnEmptyListOfPersons() {
        Persons persons = RestAssured.get("/persons").then()
                .statusCode(200)
                .extract()
                .as(Persons.class);
        assertThat(List.of()).isEqualTo(persons.persons());
    }

    @Test
    @Order(2)
    @DisplayName("addPersons should return response 201 and getAllPersons should return list with person")
    void addPersonShouldReturnResponse201AndGetPersonsShouldReturnListWithPerson() {
        RequestSpecification request = setUpRequest("{\"name\" : \"Kjell\",\"age\" : \"25\", \"profession\" : \"Kriminell\"}");

        request.post().then().statusCode(201);
        Persons persons = RestAssured.get("/persons").as(Persons.class);

        assertThat(List.of(new PersonDto("Kjell", 25, "Kriminell"))).isEqualTo(persons.persons());
    }
    @Test
    @Order(3)
    @DisplayName("getPersonById should return person with that id")
    void getPersonByIdShouldReturnPersonWithThatId() {
        RequestSpecification request = setUpRequest("{\"name\" : \"Sten\",\"age\" : \"204\", \"profession\" : \"Landowner\"}");

        UUID id = getUuidFromResponse(request.post());

        PersonDto personDto = RestAssured.get("/persons/" + id).then()
                .statusCode(200)
                .extract()
                .as(PersonDto.class);

        assertThat(new PersonDto("Sten", 204, "Landowner")).isEqualTo(personDto);
    }
    @Test
    @Order(4)
    @DisplayName("updatePerson should return updated personDto with updated fields")
    void updatePersonShouldReturnUpdatedPersonDtoWithUpdatedFields(){
        RequestSpecification request = setUpRequest("{\"name\" : \"Ulla\",\"age\" : \"68\", \"profession\" : \"Gravedigger\"}");

        UUID id = getUuidFromResponse(request.post());

        String jsonString = "{\"name\" : \"Ulla\",\"age\" : \"70\", \"profession\" : \"Programmer\"}";
        PersonDto personDto = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(jsonString)
                .when()
                .patch("/persons/"+id)
                .then()
                .statusCode(200)
                .extract()
                .as(PersonDto.class);

        assertThat(new PersonDto("Ulla",70,"Programmer")).isEqualTo(personDto);
    }
    @Test
    @Order(5)
    @DisplayName("deletePerson should remove person, getAllPersons should return list without that person")
    void deletePersonShouldRemovePersonGetAllPersonsShouldReturnListWithoutThatPerson(){
        RequestSpecification request = setUpRequest("{\"name\" : \"Remy Removal\",\"age\" : \"12\", \"profession\" : \"Cleaner\"}");

        UUID id = getUuidFromResponse(request.post());

        RestAssured.delete("/persons/" + id).then()
                .statusCode(204);
        Persons persons = RestAssured.get("/persons").as(Persons.class);
        assertThat(persons.persons()).doesNotContain(new PersonDto("Remy Removal",12,"Cleaner"));
    }
    @Test
    @Order(6)
    @DisplayName("getPersonById with invalid Id should give status 404")
    void getPersonByIdWithInvalidIdShouldGiveStatus404(){
        RestAssured.get("/persons/" + 666)
                .then()
                .statusCode(404);
    }
    @Test
    @Order(7)
    @DisplayName("addPersons with invalid fields should return status 400 and response should contain ValidationErrors")
    void addPersonsWithInvalidFieldsShouldReturnStatus400AndResponseShouldContainValidationErrors(){
        RequestSpecification request = setUpRequest("{\"name\" : \"\",\"age\" : \"-124\", \"profession\" : \"\"}");

        Response response = request.post()
                .then()
                .statusCode(400)
                .extract()
                .response();

        assertThat(response.asString()).contains("{\"field\":\"age\",\"violationMessage\":\"must be greater than 0\"}","{\"field\":\"profession\",\"violationMessage\":\"must not be empty\"}","{\"field\":\"name\",\"violationMessage\":\"must not be empty\"}");
    }
    @NotNull
    private static RequestSpecification setUpRequest(String jsonString) {
        RequestSpecification request = RestAssured.given();
        request.contentType(ContentType.JSON);
        request.baseUri("http://localhost:" + port + "/jakartaeelabb/persons");
        request.body(jsonString);
        return request;
    }

    @NotNull
    private static UUID getUuidFromResponse(Response response) {
        String responseHeader = response.getHeader("Location");
        String[] splitHeader = responseHeader.split("/");
        return UUID.fromString((splitHeader[splitHeader.length - 1]));
    }
}