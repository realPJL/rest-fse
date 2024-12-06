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
    public List<Artikel> getAlleArtikel() {
        return artikelRepository.findAll();
    }

    // GET: Einen Artikel nach ID abrufen
    @GetMapping("/{id}")
    public ResponseEntity<Artikel> getArtikelById(@PathVariable Long id) {
        if (!artikelRepository.existsById(id)) {
            throw new EmptyResultDataAccessException("Artikel nicht gefunden", 1); // Fehler auslösen
        }
        if (id <= 0) {
            throw new IllegalArgumentException("ID darf nicht NULL sein!");
        }
        return artikelRepository.findById(id)
                .map(ResponseEntity::ok) // Artikel gefunden
                .orElse(ResponseEntity.notFound().build()); // Artikel nicht gefunden
    }

    // POST: Neuen Artikel hinzufügen
    @PostMapping
    public ResponseEntity<Artikel> erstelleArtikel(@RequestBody Artikel neuerArtikel) {
        // Validierung
        if (neuerArtikel.getBestand() < 0 || neuerArtikel.getPreis() < 0){
            throw new IllegalArgumentException("Bestand und/oder Preis dürfen nicht negativ sein!"); // Fehler auslösen
        }
        if (neuerArtikel.getName() == " " || neuerArtikel.getName().isEmpty() || neuerArtikel.getName() == null) {
            throw new IllegalArgumentException("Name darf nicht leer sein!");
        }
        Artikel gespeicherterArtikel = artikelRepository.save(neuerArtikel);
        return ResponseEntity.status(201).body(gespeicherterArtikel);
    }

    // PUT: Artikel aktualisieren
    @PutMapping("/{id}")
    public ResponseEntity<Artikel> aktualisiereArtikel(@PathVariable Long id, @RequestBody Artikel artikelDetails) {
        if (!artikelRepository.existsById(id)) {
            throw new EmptyResultDataAccessException("Artikel mit der ID " + id + " nicht gefunden.", 1);
        }
        return artikelRepository.findById(id).map(artikel -> {
            if (artikelDetails.getBestand() < 0 || artikelDetails.getPreis() < 0) {
                throw new IllegalArgumentException("Bestand und/oder Preis dürfen nicht negativ sein!");
            }
            if (id == null) {
                throw new IllegalArgumentException("ID darf nicht NULL sein!");
            }
            // Aktualisieren der Felder
            artikel.setName(artikelDetails.getName());
            artikel.setBestand(artikelDetails.getBestand());
            artikel.setPreis(artikelDetails.getPreis());
            Artikel aktualisiert = artikelRepository.save(artikel);
            return ResponseEntity.ok(aktualisiert);
        }).orElse(ResponseEntity.notFound().build()); // Artikel nicht gefunden
    }

    // DELETE: Artikel löschen
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteArtikel(@PathVariable Long id) {
        if (!artikelRepository.existsById(id)) {
            throw new EmptyResultDataAccessException("Artikel mit der ID " + id + " nicht gefunden.", 1);
        }
        artikelRepository.deleteById(id);
        return ResponseEntity.ok("Artikel mit ID " + id + " wurde erfolgreich gelöscht.");
    }
}
