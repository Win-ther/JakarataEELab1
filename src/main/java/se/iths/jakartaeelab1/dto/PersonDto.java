package se.iths.jakartaeelab1.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import se.iths.jakartaeelab1.entity.Person;

public record PersonDto(@NotEmpty String name, @Positive int age, @NotEmpty String profession) {

    public static PersonDto map(Person person) {
        return new PersonDto(person.getName(), person.getAge(), person.getProfession());
    }

}
