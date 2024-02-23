package se.iths.jakartaeelab1.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class PersonRepository {

    @PersistenceContext(unitName = "mysql")
    EntityManager entityManager;



}
