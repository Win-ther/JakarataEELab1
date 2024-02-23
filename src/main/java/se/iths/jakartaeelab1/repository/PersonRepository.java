package se.iths.jakartaeelab1.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import se.iths.jakartaeelab1.entity.Person;

@ApplicationScoped
public class PersonRepository {

    @PersistenceContext(unitName = "mysql")
    EntityManager entityManager;

    @Transactional
    public Person createPerson(Person person) {
        entityManager.persist(person);
        return person;
    }

    public List<Person> readAllPerson() {
        return entityManager
                .createQuery("select p from Person p", Person.class)
                .getResultList();
    }

    public Person findPersonById(UUID id) {
        return entityManager.find(Person.class, id);
    }

    public Person updatePerson(Person updatedPerson) {
        return entityManager.merge(updatedPerson);
    }

    public void deletePerson(Person person) {
        entityManager.remove(person);
    }

}
