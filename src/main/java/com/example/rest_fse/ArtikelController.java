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

    // POST: Neuen Artikel hinzufügen
    @PostMapping
    public ResponseEntity<Artikel> erstelleArtikel(@RequestBody Artikel neuerArtikel) {
        // Validierung der Eingabedaten
        if (neuerArtikel.getBestand() < 0 || neuerArtikel.getBestand() > 2147483647) {
            throw new IllegalArgumentException("Bestand hat einen ungültigen Wert als Eingabe.");
        }
        if (neuerArtikel.getPreis() < 0 || neuerArtikel.getPreis() > 1000000) {
            throw new IllegalArgumentException("Preis hat einen ungültigen Wert als Eingabe.");
        }
        if (neuerArtikel.getName().trim().isEmpty() || neuerArtikel.getName() == null) {
            throw new IllegalArgumentException("Name darf nicht leer sein.");
        }
        if (!neuerArtikel.getName().matches("^[a-zA-Z\s]+$")) {
            throw new IllegalArgumentException("Name darf nur Buchstaben und Leerzeichen enthalten.");
        }

        // Artikel speichern und ausgeben
        Artikel gespeicherArtikel = artikelRepository.save(neuerArtikel);
        return ResponseEntity.status(201).body(gespeicherArtikel);
    }

    // PUT: Artikel aktualisieren
    @PutMapping("/{id}")
    public ResponseEntity<Artikel> aktualisiereArtikel(@PathVariable Long id, @RequestBody Artikel artikelDetails) {
        if (id <= 0) {
            throw new IllegalArgumentException("Die Artikel-ID muss größer als 0 sein.");
        }
        
        // Eingabedaten validieren
        if (artikelDetails.getBestand() < 0 || artikelDetails.getBestand() > 2147483647) {
            throw new IllegalArgumentException("Bestand hat einen ungültigen Wert als Eingabe.");
        }
        if (artikelDetails.getPreis() < 0 || artikelDetails.getPreis() > 1000000) {
            throw new IllegalArgumentException("Preis hat einen ungültigen Wert als Eingabe.");
        }
        if (artikelDetails.getName().trim().isEmpty() || artikelDetails.getName() == null) {
            throw new IllegalArgumentException("Name darf nicht leer sein.");
        }
        if (!artikelDetails.getName().matches("^[a-zA-Z\s]+$")) {
            throw new IllegalArgumentException("Name darf nur Buchstaben.");
        }
        
        return artikelRepository.findById(id).map(artikel -> {
            artikel.setName(artikelDetails.getName());
            artikel.setBestand(artikelDetails.getBestand());
            artikel.setPreis(artikelDetails.getPreis());
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
}
