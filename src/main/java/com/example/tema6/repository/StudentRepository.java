package com.example.tema6.repository;

import  com.example.tema6.classe.Student;

public class StudentRepository extends InMemoryRepository<Student>{

    public StudentRepository()
    {
        super();
    }

    /**
     * @param id das Id eines Objektes aus der Liste "repoList"
     * @return der Student mit der Id "id"
     */
    @Override
    public Student findOne(Long id){
        Student studentToFind = this.repoList.stream()
                .filter(student -> student.getStudentID() == id)
                .findFirst()
                .orElse(null);

        return studentToFind;
    }

    /**
     *
     * @param entity ein Objekt von Typ Student
     * @return eine aktualisierte Version des Objektes
     */
    @Override
    public Student update(Student entity) {
        Student studentToUpdate = this.repoList.stream()
                .filter(student -> student.getStudentID() == entity.getStudentID())
                .findFirst()
                .orElseThrow();

        studentToUpdate.setPersonID(entity.getPersonID());
        studentToUpdate.setFirstName(entity.getFirstName());
        studentToUpdate.setLastName(entity.getLastName());
        studentToUpdate.setTotalCredits(entity.getTotalCredits());
        studentToUpdate.setEnrolledCourses(entity.getEnrolledCourses());

        return studentToUpdate;
    }
}