package se.iths.jakartaeelab1.resource;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.bouncycastle.cert.ocsp.Req;
import se.iths.jakartaeelab1.dto.PersonDto;
import se.iths.jakartaeelab1.dto.Persons;
import se.iths.jakartaeelab1.entity.Person;
import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class PersonIT {

    @Container
    public static ComposeContainer testEnvironment = new ComposeContainer(new File("src/test/resources/compose-test.yml"))
            .withExposedService("db",3306)
            .withExposedService("wildfly",8080, Wait.forHttp("/jakartaeelabb/persons")
                    .forStatusCode(200))
            .withLocalCompose(true);
    static String host;
    static int port;

    @BeforeAll
    static void beforeAll(){
        host = testEnvironment.getServiceHost("wildfly",8080);
        port = testEnvironment.getServicePort("wildfly",8080);
    }
    @BeforeEach
    void before(){
        RestAssured.baseURI = "http://" + host + "/jakartaeelabb";
        RestAssured.port = port;
    }
    @Test
    @DisplayName("getAllPersonsShouldReturnEmptyListOfPersons")
    void getAllPersonsShouldReturnEmptyListOfPersons(){
        Persons persons = RestAssured.get("/persons").then()
                .statusCode(200)
                .extract()
                .as(Persons.class);
        assertEquals(List.of(),persons.persons());
    }
    @Test
    @DisplayName("addPersonShouldReturnListWithSamePerson")
    void addPersonShouldReturnListWithSamePerson(){
        PersonDto alf = new PersonDto("Alf",25,"Kriminell");
        String jsonString = "{\"name\" : \"alf\",\"age\" : \"25\", \"profession\" : \"Kriminell\"}";
        RequestSpecification request = RestAssured.given();
        request.contentType(ContentType.JSON);
        request.baseUri("http://localhost:8080/jakartaeelabb/persons");
        request.body(jsonString);
        Response response = request.post();
        System.out.println(response.asString());
        ValidatableResponse validatableResponse = response.then();
        validatableResponse.statusCode(201);
        System.out.println(response.getStatusCode());
        System.out.println(response.getTime());

    }
 /*   @Test
    @DisplayName("getPersonByIdShouldReturnPersonWithThatId")
    void getPersonByIdShouldReturnPersonWithThatId(){

    }*/
}