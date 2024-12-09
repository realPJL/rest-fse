package com.example.rest_fse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.dao.EmptyResultDataAccessException;


@RestController
@RequestMapping("/api/artikel")
public class ArtikelController {

    @Autowired
    private ArtikelRepository artikelRepository;

    // GET: Alle Artikel abrufen
    @GetMapping
    public ResponseEntity<Object> getAllArtikel() {
        List<Artikel> artikelListe = artikelRepository.findAll();
        if (artikelListe.isEmpty()) {
            // Rückgabe einer Fehlermeldung, wenn keine Artikel gefunden wurden
            throw new EmptyResultDataAccessException("Keine Artikel gvorhanden bzw. im Inventar", 1);
        }
        // Rückgabe der Artikelliste
        return ResponseEntity.ok(artikelListe);
    }

    // GET: Einen Artikel nach ID abrufen
    @GetMapping("/{id}")
    public ResponseEntity<Artikel> getArtikelById(@PathVariable Long id) {
        // Validierung der ID
        if (id <= 0) {
            throw new IllegalArgumentException("Die Artikel-ID muss größer als 0 sein.");
        }
        return artikelRepository.findById(id)
                .map(ResponseEntity::ok) // Artikel gefunden
                .orElseThrow(() -> new EmptyResultDataAccessException("Artikel mit der ID " + id + " nicht gefunden.", 1)); // Artikel nicht gefunden
    }

    // GET: Artikel basierend auf Lagerort abrufen
    @GetMapping("/lagerort")
    public ResponseEntity<List<Artikel>> getArtikelByLagerort(@RequestParam String lagerort) {
        if (lagerort == null || lagerort.trim().isEmpty()) {
            throw new IllegalArgumentException("Lagerort darf nicht leer sein.");
        }
        if (!lagerort.matches("^[a-zA-Z\s]+$")) {
            throw new IllegalArgumentException("Lagerort darf nur Buchstaben enthalten.");
        }
        if (!artikelRepository.existsByLagerort(lagerort)) {
            throw new EmptyResultDataAccessException("Lagerort " + lagerort + " nicht gefunden.", 1);
        }

        List<Artikel> artikelListe = artikelRepository.findByLagerort(lagerort);
        if (artikelListe.isEmpty()) {
            throw new EmptyResultDataAccessException("Keine Artikel im Lagerort " + lagerort + " vorhanden.", 1);
        }
        return ResponseEntity.ok(artikelListe);
    }

    // POST: Neuen Artikel hinzufügen
    @PostMapping
    public ResponseEntity<Artikel> addArtikel(@RequestBody Artikel neuerArtikel) {
        // Validierung der Eingabedaten
        validateArtikel(neuerArtikel);

        // Artikel speichern und ausgeben
        Artikel gespeicherArtikel = artikelRepository.save(neuerArtikel);
        return ResponseEntity.status(201).body(gespeicherArtikel);
    }

    // PUT: Artikel aktualisieren
    @PutMapping("/{id}")
    public ResponseEntity<Artikel> updateArtikel(@PathVariable Long id, @RequestBody Artikel artikelDetails) {
        if (id <= 0) {
            throw new IllegalArgumentException("Die Artikel-ID muss größer als 0 sein.");
        }
        
        // Eingabedaten validieren
        validateArtikel(artikelDetails);

        return artikelRepository.findById(id).map(artikel -> {
            artikel.setName(artikelDetails.getName());
            artikel.setBestand(artikelDetails.getBestand());
            artikel.setEinzelpreis(artikelDetails.getEinzelpreis());
            Artikel aktualisiert = artikelRepository.save(artikel);
            return ResponseEntity.ok(aktualisiert);
        }).orElseThrow(() -> new EmptyResultDataAccessException("Artikel mit der ID " + id + " nicht gefunden.", 1)); // Artikel gefunden
    }

    // DELETE: Artikel löschen
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteArtikel(@PathVariable Long id) {
        // Validierung der ID
        if (id <= 0) {
            throw new IllegalArgumentException("Artikel-ID muss größer als 0 sein.");
        }
        if (!artikelRepository.existsById(id)) {
            throw new EmptyResultDataAccessException("Artikel mit der ID " + id + " nicht gefunden.", 1);
        }

        artikelRepository.deleteById(id);
        return ResponseEntity.ok("Artikel mit der ID " + id + " gelöscht.");
    }

    private void validateArtikel(Artikel artikel) {
        // Validierung der Eingabedaten:
    
        // Bestand hat einen ungültigen Wert
        if (artikel.getBestand() < 0 || artikel.getBestand() > 5000000){
            throw new IllegalArgumentException("Bestand hat einen ungültigen Wert als Eingabe.");
        }
        // Preis hat einen ungültigen Wert
        if (artikel.getEinzelpreis() < 0 || artikel.getEinzelpreis() > 1000000){
            throw new IllegalArgumentException("Preis hat einen ungültigen Wert als Eingabe.");
        }
        // Name darf nicht leer sein
        if (artikel.getName().trim().isEmpty() || artikel.getName() == null) {
            throw new IllegalArgumentException("Name darf nicht leer sein.");
        }
        // Name darf nur aus Buchstaben bestehen
        if (!artikel.getName().matches("^[a-zA-Z\\s]+$")) {
            throw new IllegalArgumentException("Name darf nur aus Buchstaben bestehen.");
        }
        // Lagerort darf nicht leer sein
        if (artikel.getLagerort().trim().isEmpty() || artikel.getLagerort() == null) {
            throw new IllegalArgumentException("Lagerort darf nicht leer sein."); 
        }
        // Lagerort darf nur aus Buchstaben bestehen
        if (!artikel.getLagerort().matches("^[a-zA-Z\s]+$")) {
            throw new IllegalArgumentException("Lagerort darf nur aus Buchstaben bestehen.");
        }
    }
}
