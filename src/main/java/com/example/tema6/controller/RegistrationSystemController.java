package com.example.tema6.controller;


import  com.example.tema6.exception.*;
import  com.example.tema6.classe.*;
import  com.example.tema6.repository.LehrerRepository;
import  com.example.tema6.repository.RegistrationSystem;
import  com.example.tema6.repository.StudentRepository;
import com.example.tema6.repository.KursRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class RegistrationSystemController {
    private final RegistrationSystem registrationSystem;

    public RegistrationSystemController(){
        this.registrationSystem = new RegistrationSystem();
        this.controller_addPerson(1L, "Zoe", "Miller");
        this.controller_addPerson(2L, "Alice", "Hart");
        this.controller_addPerson(3L, "Alice", "Miller");
        this.controller_addPerson(4L, "Tom", "John");
        this.controller_addPerson(5L, "Jack", "Storm");

        this.controller_addStudent(1L, 1111L);
        this.controller_addStudent(2L, 1112L);

        this.controller_addLehrer(4L, 11L);
        this.controller_addLehrer(5L, 12L);

        this.controller_addKurs("BD", 11L, 100L, 30, 5);
        this.controller_addKurs("BD2", 12L, 101L, 31, 6);
        this.controller_addKurs("BD3", 11L, 102L, 32, 7);
        this.controller_addKurs("BD4", 11L, 103L, 30, 8);
        this.controller_addKurs("BD5", 12L, 104L, 31, 9);
        this.controller_addKurs("BD6", 11L, 105L, 32, 10);
    }

    /**
     * @param kursID
     * @param StudentID
     * @throws RegisterException
     */
    public void controller_register(long kursID, long StudentID) throws RegisterException {
        registrationSystem.register(kursID, StudentID);
    }

    /**
     * @param kursID
     * @param StudentID
     * @throws RegisterException
     */
    public void controller_unregister(long kursID, long StudentID) throws RegisterException {
        registrationSystem.unregister(kursID, StudentID);
    }

    public StudentRepository controller_studentRepository(){
        return this.registrationSystem.getStudentRepository();
    }
    public LehrerRepository controller_lehrerRepository(){return this.registrationSystem.getLehrerRepository();}
    public KursRepository controllerKursRepository(){
        return this.registrationSystem.getKursRepository();
    }


    public List<Kurs> controller_getAllKurse(){
        return registrationSystem.getAllCourses();
    }

    public List<Person> controller_getAllPersons() {
        return registrationSystem.getAllPersons();
    }

    public List<Student> controller_getAllStudents() {
        return registrationSystem.getAllStudents();
    }

    public List<Lehrer> controller_getAllLehrer() {
        return registrationSystem.getAllLehrer();
    }

    public HashMap<Integer, Kurs> controller_retrieveCoursesWithFreePlaces() {
        return registrationSystem.retrieveCoursesWithFreePlaces();
    }

    /**
     * @param KursID
     * @throws ExistException
     */
    public List<Long> controller_retrieveStudentsEnrolledForACourse(long KursID) throws ExistException {
        return registrationSystem.retrieveStudentsEnrolledForACourse(KursID);
    }

    /**
     * @param KursID
     * @param newCredit
     * @throws RegisterException
     * @throws ExistException
     */
    public void controller_changeCreditFromACourse(long KursID, int newCredit) throws RegisterException, ExistException{
        registrationSystem.changeCreditFromACourse(KursID, newCredit);
    }

    /**
     * @param LehrerID
     * @param KursID
     * @throws DeleteKursFromLehrerException
     * @throws RegisterException
     */
    public void controller_deleteKursFromLehrer(long LehrerID, long KursID) throws DeleteKursFromLehrerException, RegisterException {
        registrationSystem.deleteKursFromLehrer(LehrerID, KursID);
    }

    public List<Student> controller_sortListeStudenten() {
        Collections.sort(registrationSystem.getAllStudents(), new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return o1.getPersonID().compareTo(o2.getPersonID());
            }
        });

        return registrationSystem.getAllStudents();
    }

    public List<Kurs> controller_sortListeKurse(){
        Collections.sort(registrationSystem.getAllCourses(), new Comparator<Kurs>() {
            @Override
            public int compare(Kurs o1, Kurs o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        return registrationSystem.getAllCourses();
    }

    public List<Student> controller_filterListeStudenten(){
        List<Student> studentList = this.controller_getAllStudents().stream()
                .filter(student -> student.getEnrolledCourses().size() >=2).collect(Collectors.toList());

        return studentList;
    }

    public List<Kurs> controller_filterListeKurse(){
        List<Kurs> kursList = this.controller_getAllKurse().stream()
                .filter(kurs -> kurs.getMaxEnrollment() > 30).collect(Collectors.toList());

        return kursList;
    }

    /**
     * @param personID
     * @param firstName
     * @param lastName
     */
    public void controller_addPerson(Long personID, String firstName, String lastName){
        this.registrationSystem.getPersonRepository()
                .save(new Person(personID, firstName, lastName));
    }

    /**
     * @param PersonID
     * @param StudentID
     */
    public void controller_addStudent(Long PersonID, Long StudentID){
        if (registrationSystem.getPersonRepository().findOne(PersonID) != null) {
            registrationSystem.getStudentRepository()
                    .save(new Student(registrationSystem.getPersonRepository().findOne(PersonID), StudentID));
        }
    }

    /**
     * @param PersonID
     * @param LehrerID
     */
    public void controller_addLehrer(Long PersonID, Long LehrerID) {
        if (registrationSystem.getPersonRepository().findOne(PersonID) != null){
            registrationSystem.getLehrerRepository()
                    .save(new Lehrer(registrationSystem.getPersonRepository().findOne(PersonID), LehrerID));
        }

    }

    /**
     * @param Name
     * @param IdLehrer
     * @param IdKurs
     * @param MaxEnrollment
     * @param Credits
     */
    public void controller_addKurs(String Name, Long IdLehrer, Long IdKurs, int MaxEnrollment, int Credits){
        if (registrationSystem.getLehrerRepository().findOne(IdLehrer) != null){
            registrationSystem.getKursRepository()
                    .save(new Kurs(Name, IdLehrer, IdKurs, MaxEnrollment, Credits));
        }
    }
}