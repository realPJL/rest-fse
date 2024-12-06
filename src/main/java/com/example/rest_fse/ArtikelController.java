package com.example.rest_fse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/artikel")
public class ArtikelController {

    @Autowired
    private ArtikelRepository artikelRepository;


    // GET: Alle Artikel abrufen
    @GetMapping
    public ResponseEntity<Object> getAlleArtikel() {
        List<Artikel> artikelListe = artikelRepository.findAll();
        if (artikelListe.isEmpty()) {
            // Rückgabe einer Fehlermeldung, wenn keine Artikel vorhanden sind
            throw new EmptyResultDataAccessException("Keine Artikel vorhanden bzw. im Inventar.",1);
        }
        // Rückgabe der Artikelliste
        return ResponseEntity.ok(artikelListe);
    }



    // GET: Einen Artikel nach ID abrufen
    @GetMapping("/{id}")
    public ResponseEntity<Artikel> getArtikelById(@PathVariable Long id) {
        // Validierung der ID
        /*if (id <= 0) {
            throw new IllegalArgumentException("Die Artikel-ID muss größer als 0 sein.");
        } */
        return artikelRepository.findById(id)
                
                // Artikel gefunden
                .map(artikel -> ResponseEntity.ok(artikel)) 

                // Artikel nicht gefunden
                .orElseThrow(() -> new EmptyResultDataAccessException("Artikel mit der ID " + id + " nicht gefunden.", 1));
    }



    // POST: Neuen Artikel hinzufügen
    @PostMapping
    public ResponseEntity<Artikel> erstelleArtikel(@RequestBody Artikel neuerArtikel) {
        
        // Validierung der Eingabedaten:

        // Bestand hat einen ungültigen Wert
        if (neuerArtikel.getBestand() < 0 || neuerArtikel.getBestand() > 2147483647){
            throw new IllegalArgumentException("Bestand hat einen ungültigen Wert als Eingabe.");
        }
        // Preis hat einen ungültigen Wert
        if (neuerArtikel.getPreis() < 0 || neuerArtikel.getPreis() > 1000000){
            throw new IllegalArgumentException("Preis hat einen ungültigen Wert als Eingabe."); 
        }
        // Name darf nicht leer sein
        if (neuerArtikel.getName().trim().isEmpty() || neuerArtikel.getName() == null) {
            throw new IllegalArgumentException("Name darf nicht leer sein."); 
        }
        // Name darf nur aus Buchstaben bestehen
        if (!neuerArtikel.getName().matches("^[a-zA-Z\\s]$")) {
            throw new IllegalArgumentException("Name darf nur aus Buchstaben bestehen.");
        }
    

        // Artikel speichern und ausgeben
        Artikel gespeicherterArtikel = artikelRepository.save(neuerArtikel);
        return ResponseEntity.status(201).body(gespeicherterArtikel);
    }



    // PUT: Artikel aktualisieren
    @PutMapping("/{id}")
    public ResponseEntity<Artikel> aktualisiereArtikel(@PathVariable Long id, @RequestBody Artikel artikelDetails) {
         // Validierung der ID
        if (id <= 0) {
            throw new IllegalArgumentException("Die Artikel-ID muss größer als 0 sein.");
        }

        // Validierung der Eingabedaten:

        // Bestand hat einen ungültigen Wert
        if (artikelDetails.getBestand() < 0 || artikelDetails.getBestand() > 2147483647){
            throw new IllegalArgumentException("Bestand hat einen ungültigen Wert als Eingabe.");
        }
        // Preis hat einen ungültigen Wert
        if (artikelDetails.getPreis() < 0 || artikelDetails.getPreis() > 1000000){
            throw new IllegalArgumentException("Preis hat einen ungültigen Wert als Eingabe.");
        }
        // Name darf nicht leer sein
        if (artikelDetails.getName().trim().isEmpty() || artikelDetails.getName() == null) {
            throw new IllegalArgumentException("Name darf nicht leer sein.");
        }
        // Name darf nur aus Buchstaben bestehen
        if (!artikelDetails.getName().matches("^[a-zA-Z\\s]$")) {
            throw new IllegalArgumentException("Name darf nur aus Buchstaben bestehen.");
        }

        // Artikel suchen, artikelDetails aktualisieren, speichern und ausgeben
        return artikelRepository.findById(id).map(artikel -> {
            artikel.setName(artikelDetails.getName());
            artikel.setBestand(artikelDetails.getBestand());
            artikel.setPreis(artikelDetails.getPreis());
            Artikel aktualisiert = artikelRepository.save(artikel);
            return ResponseEntity.ok(aktualisiert);

        // Exception: Artikel nicht gefunden
        }).orElseThrow(() -> new EmptyResultDataAccessException("Artikel mit der ID "  + id +  " nicht gefunden.", 1));
    }



    // DELETE: Artikel löschen
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteArtikel(@PathVariable Long id) {
        
        // Validierung der ID
        if (id <= 0) {
            throw new IllegalArgumentException("Die Artikel-ID muss größer als 0 sein.");
        }
        if (!artikelRepository.existsById(id)) {
            throw new EmptyResultDataAccessException("Artikel mit der ID " + id + " nicht gefunden.", 1);
        }
        // Artikel wird gelöscht
        artikelRepository.deleteById(id);
        // Bestätigung der Löschung
        return ResponseEntity.ok("Artikel mit ID " + id + " wurde erfolgreich gelöscht.");
    }
}
