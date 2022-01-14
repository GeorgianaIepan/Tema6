package com.example.tema6.classe;


import java.util.ArrayList;
import java.util.List;

public class Lehrer extends com.example.tema6.classe.Person {
    private long lehrerID;
    private List<Long> kurse;

    /**
     * @param person
     * @param lehrerID
     */
    public Lehrer(Person person, long lehrerID) {
        super(person.getPersonID(), person.getFirstName(), person.getLastName());
        this.lehrerID = lehrerID;
        this.kurse = new ArrayList<>();
    }

    public Lehrer(Person person, long lehrerID, List<Long> kurse) {
        super(person.getPersonID(), person.getFirstName(), person.getLastName());
        this.lehrerID = lehrerID;
        this.kurse = kurse;
    }

    @Override
    public String toString() {
        return "Lehrer{" +
                "lehrerID=" + lehrerID +
                ", kurse=" + kurse +
                "} " + super.toString();
    }


    @Override
    public Long getPersonID() {
        return super.getPersonID();
    }

    @Override
    public String getFirstName() {
        return super.getFirstName();
    }

    @Override
    public String getLastName() {
        return super.getLastName();
    }

    public long getLehrerID() { return lehrerID; }

    public List<Long> getKurse() { return kurse; }


    @Override
    public void setPersonID(Long personID) {
        super.setPersonID(personID);
    }

    @Override
    public void setFirstName(String firstName) {
        super.setFirstName(firstName);
    }

    @Override
    public void setLastName(String lastName) {
        super.setLastName(lastName);
    }

    public void setLehrerID(long lehrerID) { this.lehrerID = lehrerID; }

    public void setKurse(List<Long> kurse) { this.kurse = kurse; }
}
