package com.example.rest_fse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

//Für die Datenbankanbindung & Standard-Methoden wie save, findAll, findById werden automatisch bereitgestellt
@Repository
public interface ArtikelRepository extends JpaRepository<Artikel, Long> {
    List<Artikel> findByLagerort(String lagerort);
    boolean existsByLagerort(String lagerort);
}
