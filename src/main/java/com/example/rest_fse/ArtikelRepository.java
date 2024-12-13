package com.example.rest_fse;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Für die Datenbankanbindung & Standard-Methoden wie save, findAll, findById werden automatisch bereitgestellt
@Repository
public interface ArtikelRepository extends JpaRepository<Artikel, Long> {
    // Methode soll die Artikelliste abrufen
    List<Artikel> findByLagerort(String lagerort);
    // Methode soll überprüfen, ob ein bestimmter Lagerort existiert
    boolean existsByLagerort(String lagerort);
}
