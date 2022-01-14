package com.example.tema6.repository;

import  com.example.tema6.classe.Person;

public class PersonRepository extends InMemoryRepository<Person> {
    public PersonRepository()
    {
        super();
    }

    /**
     * @param id das Id eines Objektes aus der Liste "repoList"
     * @return die Person mit der Id "id"
     */
    @Override
    public Person findOne(Long id) {
        Person personToFind = this.repoList.stream()
                .filter(person -> person.getPersonID() == id)
                .findFirst()
                .orElse(null);
        return personToFind;
    }

    /**
     *
     * @param entity ein Objekt von Typ Person
     * @return eine aktualisierte Version des Objektes
     */
    @Override
    public Person update(Person entity) {
        Person personToUpdate = this.repoList.stream()
                .filter(person -> person.getPersonID() == entity.getPersonID())
                .findFirst()
                .orElseThrow();

        personToUpdate.setFirstName(entity.getFirstName());
        personToUpdate.setLastName(entity.getLastName());

        return personToUpdate;
    }

}