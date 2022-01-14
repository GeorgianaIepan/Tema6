package com.example.tema6.repository;


import  com.example.tema6.exception.*;
import  com.example.tema6.classe.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RegistrationSystem {
    private final KursRepository vorlesungRepository;
    private final PersonRepository personRepository;
    private final StudentRepository studentRepository;
    private final LehrerRepository lehrerRepository;
    private final EnrolledRepository enrolledRepository;

    /**
     * wir erstellen ein neues Objekt von Typ RegistrationSystem
     */
    public RegistrationSystem(){
        this.vorlesungRepository = new KursRepository();
        this.personRepository = new PersonRepository();
        this.studentRepository = new StudentRepository();
        this.lehrerRepository = new LehrerRepository();
        this.enrolledRepository = new EnrolledRepository(vorlesungRepository, studentRepository);
    }
    /**
     * @return alle Elementen aus der "vorlesungRepository"
     */
    public KursRepository getKursRepository(){
        return this.vorlesungRepository;
    }

    /**
     * @return alle Elementen aus der "personRepository"
     */
    public PersonRepository getPersonRepository() {
        return personRepository;
    }

    /**
     * @return alle Elementen aus der "studentRepository"
     */
    public StudentRepository getStudentRepository(){
        return this.studentRepository;
    }

    /**
     * @return alle Elementen aus der "lehrerRepository"
     */
    public LehrerRepository getLehrerRepository(){
        return this.lehrerRepository;
    }


    /**
     * @param VorlesungID eine "Long" Zahl, die ein "Vorlesung" Id entspricht
     * @param StudentID eine "Long" Zahl, die ein "Student" Id entspricht
     * @return der Student meldet sich fur die gegebene Vorlesung an
     * @throws RegisterException falls der Student oder die Vorlesung nicht in der Repository Liste sind
     *                     falls es gar kein verfugbar Platz dur der Vorlesung gibt
     *                     falls der Anzahl der Credits des Studenten grosser als 30
     *
     * wir aktualisieren die Listen aus der Student-, Vorlesung- und LehrerRepository
     */
    public boolean register(long VorlesungID, long StudentID) throws RegisterException {
        String message = "Unerfüllte Bedingungen: ";
        Kurs vorlesung = this.vorlesungRepository.findOne(VorlesungID);
        Student student = this.studentRepository.findOne(StudentID);

        if (vorlesung == null || student == null)
            throw new RegisterException(message + "Der Student und/oder die Vorlesung sind/ist nicht in der Liste.");

        if (vorlesung.getStudentsEnrolled().size() > vorlesung.getMaxEnrollment())
            throw new RegisterException(message + "Keine freie Plätze.");
        else if (student.getTotalCredits() + vorlesung.getCredits() > 30)
            throw new RegisterException(message + "Anzahl von Credits übersprungen.");
        else if (this.enrolledRepository.findOne(vorlesung.getKursID(), student.getStudentID()))
            throw new RegisterException(message + "Der Student nimmt an dieser Vorlesung teil.");
        else {
            this.enrolledRepository.save(vorlesung.getKursID(), student.getStudentID());
        }

        return true;
    }

    /**
     *
     * @param VorlesungID eine "Long" Zahl, die ein "Vorlesung" Id entspricht
     * @param StudentID eine "Long" Zahl, die ein "Student" Id entspricht
     *
     * wir loschen die Vorlesung aus der "Vorlesung" Liste des Studenten
     * wir loschen der Student aus der "Studenten" Liste der Vorlesung
     * wir aktualisieren die Listen aus der Student- und VorlesungRepository
     *
     * @throws RegisterException falls Der Student und/oder die Vorlesung sind/ist nicht in der Liste
     */
    public void unregister(long VorlesungID, long StudentID) throws RegisterException {
        String message = "Unerfüllte Bedingungen: ";
        Kurs vorlesung = this.vorlesungRepository.findOne(VorlesungID);
        Student student = this.studentRepository.findOne(StudentID);

        if (vorlesung == null || student == null)
            throw new RegisterException(message + "Der Student und/oder die Vorlesung sind/ist nicht in der Liste.");

        this.enrolledRepository.delete(VorlesungID, StudentID);
    }

    /**
     *
     * @return ein HashMap mit der Vorlesungen, die freie Platze haben und deren Anzahl
     */
    public HashMap<Integer, Kurs> retrieveCoursesWithFreePlaces() {
        HashMap<Integer, Kurs> map = new HashMap<Integer, Kurs>();
        int freePlatz = 0;
        for (Kurs v: vorlesungRepository.findAll()) {
            if (v.getMaxEnrollment() > v.getStudentsEnrolled().size()) {
                freePlatz = v.getMaxEnrollment() - v.getStudentsEnrolled().size();
                map.put(freePlatz, v);
            }

        }

        return map;
    }

    /**
     * @param VorlesungID eine "Long" Zahl, die ein "Vorlesung" Id entspricht
     * @return eine Liste von Studenten, die an der gegebenen Vorlesung teilnehmen
     */
    public List<Long> retrieveStudentsEnrolledForACourse(long VorlesungID){
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < this.vorlesungRepository.repoList.size(); i++) {
            if (this.vorlesungRepository.repoList.get(i).getKursID() == VorlesungID){
                list.addAll(this.vorlesungRepository.repoList.get(i).getStudentsEnrolled());
                break;
            }
        }
        return list;
    }

    /**
     * @return alle Elemente aus der "vorlesungRepository"
     */
    public List<Kurs> getAllCourses() {
        return vorlesungRepository.findAll();
    }

    /**
     * @return alle Elemente aus der "personRepository"
     */
    public List<Person> getAllPersons(){return personRepository.findAll();}

    /**
     * @return alle Elemente aus der "studentRepository"
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * @return alle Elemente aus der "lehrerRepository"
     */
    public List<Lehrer> getAllLehrer() {
        List<Long> vorlesungList;
        Lehrer lehrer, newLehrer;
        Kurs vorlesung;
        for (int i = 0; i < this.lehrerRepository.findAll().size(); i++) {
            vorlesungList = new ArrayList<>();
            lehrer = this.lehrerRepository.repoList.get(i);
            for (int j = 0; j < this.vorlesungRepository.findAll().size(); j++) {
                vorlesung = this.vorlesungRepository.repoList.get(j);
                if (lehrer.getLehrerID() == vorlesung.getLehrer())
                    vorlesungList.add(vorlesung.getKursID());
            }

            newLehrer = new Lehrer(
                    new Person(lehrer.getPersonID(),
                            lehrer.getFirstName(),
                            lehrer.getLastName()),
                    lehrer.getLehrerID(),
                    vorlesungList
            );

            this.lehrerRepository.update(newLehrer);
        }

        return this.lehrerRepository.findAll();
    }

    /**
     *
     * @param VorlesungID eine "Long" Zahl, die ein "Vorlesung" Id entspricht
     * @param newCredit die neue Anzahl von Credits
     * @throws RegisterException falls die alte Studenten nicht mehr an der Vorlesung teilnehmen konnten
     *      wir andern die Anzahl der Credits der Vorlesung
     *      wir "unregister" alle Studenten aus der Vorlesung
     *      wir aktualisieren die Vorlesung
     *      wir "register" die alte Studenten zu der Vorlesung
     */
    public void changeCreditFromACourse(long VorlesungID, int newCredit) throws RegisterException {
        String message = "Unerfüllte Bedingungen: ";
        Kurs vorlesung = this.vorlesungRepository.findOne(VorlesungID);
        if (vorlesung == null)
            throw new RegisterException(message + "Die Vorlesung ist nicht in der Liste.");

        int oldCredit = vorlesung.getCredits();
        Kurs newVorlesung = new Kurs(vorlesung.getName(),
                vorlesung.getLehrer(),
                vorlesung.getKursID(),
                vorlesung.getMaxEnrollment(),
                newCredit);

        Student student, newStudent;
        for (Long studentID: vorlesung.getStudentsEnrolled()) {
            student = this.studentRepository.findOne(studentID);
            if (student.getTotalCredits() - oldCredit + newCredit > 30)
                unregister(VorlesungID, student.getStudentID());
            else {
                newStudent = new Student(new Person(student.getPersonID(),
                        student.getFirstName(),
                        student.getLastName()),
                        student.getStudentID(),
                        student.getTotalCredits() - oldCredit + newCredit,
                        student.getEnrolledCourses());
                this.studentRepository.update(newStudent);
            }

        }

        this.vorlesungRepository.update(newVorlesung);
    }

    /**
     *
     * @param LehrerID eine "Long" Zahl, die ein "Lehrer" Id entspricht
     * @param VorlesungID eine "Long" Zahl, die ein "Vorlesung" Id entspricht
     * @return die Loschung der Vorlesung von dem Lehrer
     *         wir "unregister" alle Studenten aus der Vorlesung
     *         wir aktualisieren die "Vorlesung" Liste des Lehrers
     */
    public boolean deleteKursFromLehrer(long LehrerID, long VorlesungID) throws DeleteKursFromLehrerException {
        String message = "Unerfüllte Bedingungen: ";
        Lehrer lehrer = this.lehrerRepository.findOne(LehrerID);
        Kurs vorlesung = this.vorlesungRepository.findOne(VorlesungID);

        if (lehrer == null || vorlesung == null)
            throw new DeleteKursFromLehrerException(message + "Der Lehrer und/oder die Vorlesung sind/ist nicht in der Liste.");

        if (vorlesung.getLehrer() != lehrer.getLehrerID())
            throw new DeleteKursFromLehrerException(message + "Der Lehrer unterrichtet nicht diese Vorlesung.");

        for (Long student: vorlesung.getStudentsEnrolled()) {
            try {
                unregister(vorlesung.getKursID(), student);
            } catch (RegisterException e) {
                System.out.println(message + e.getMessage());
            }
        }

        List<Long> vorlesungList = this.lehrerRepository.findOne(LehrerID).getKurse();
        vorlesungList.remove(vorlesung);
        Lehrer newLehrer = new Lehrer(new Person(lehrer.getPersonID(),
                lehrer.getFirstName(),
                lehrer.getLastName()),
                lehrer.getLehrerID(),
                vorlesungList);

        this.lehrerRepository.update(newLehrer);
        this.vorlesungRepository.delete(vorlesung);

        return true;
    }

}