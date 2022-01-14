package com.example.tema6.repository;

import com.example.tema6.classe.Kurs;

public class KursRepository extends InMemoryRepository<Kurs>{

    public KursRepository() {
        super();
    }

    /**
     * @param id das Id eines Objektes aus der Liste "repoList"
     * @return die Vorlesung mit der Id "id"
     */
    @Override
    public Kurs findOne(Long id) {
        Kurs vorlesungToFind = this.repoList.stream()
                .filter(vorlesung -> vorlesung.getKursID() == id)
                .findFirst()
                .orElse(null);

        return vorlesungToFind;
    }

    /**
     *
     * @param entity ein Objekt von Typ Vorlesung
     * @return eine aktualisierte Version des Objektes
     */
    @Override
    public Kurs update(Kurs entity) {
        Kurs vorlesungToUpdate = this.repoList.stream()
                .filter(vorlesung -> vorlesung.getKursID() == entity.getKursID())
                .findFirst()
                .orElseThrow();

        vorlesungToUpdate.setName(entity.getName());
        vorlesungToUpdate.setLehrer(entity.getLehrer());
        vorlesungToUpdate.setMaxEnrollment(entity.getMaxEnrollment());
        vorlesungToUpdate.setStudentsEnrolled(entity.getStudentsEnrolled());
        vorlesungToUpdate.setCredits(entity.getCredits());

        return vorlesungToUpdate;
    }
}
