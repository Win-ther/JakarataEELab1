package se.iths.jakartaeelab1.resource;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import se.iths.jakartaeelab1.dto.Persons;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class PersonIT {

    @Container
    public static ComposeContainer testEnvironment = new ComposeContainer(new File("src/test/resources/compose-test.yml"))
            .withExposedService("wildfly",8080, Wait.forHttp("/jakartaeelabb/persons").forStatusCode(200))
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
    @DisplayName("getAllPersons")
    void getAllPersons(){
        Persons persons = RestAssured.get("/persons").then()
                .statusCode(200)
                .extract()
                .as(Persons.class);
        assertEquals(List.of(),persons.persons());
    }
}