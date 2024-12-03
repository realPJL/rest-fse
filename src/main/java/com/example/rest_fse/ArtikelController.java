package com.example.rest_fse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public List<Artikel> getAlleArtikel() {
        return artikelRepository.findAll(); // Holt alle Artikel aus der Datenbank
    }

    // GET: Einen Artikel nach ID abrufen
    @GetMapping("/{id}")
    public ResponseEntity<Artikel> getArtikelById(@PathVariable Long id) {
        return artikelRepository.findById(id)
                .map(ResponseEntity::ok) // Artikel gefunden
                .orElse(ResponseEntity.notFound().build()); // Artikel nicht gefunden
    }

    // POST: Neuen Artikel hinzufügen
    @PostMapping
    public ResponseEntity<Artikel> erstelleArtikel(@RequestBody Artikel neuerArtikel) {
        // Validierung
        if (neuerArtikel.getBestand() < 0 || neuerArtikel.getPreis() < 0) {
            return ResponseEntity.badRequest().build(); // Ungültige Eingabe
        }
        Artikel gespeicherterArtikel = artikelRepository.save(neuerArtikel);
        return ResponseEntity.status(201).body(gespeicherterArtikel);
    }

    // PUT: Artikel aktualisieren
    @PutMapping("/{id}")
    public ResponseEntity<Artikel> aktualisiereArtikel(@PathVariable Long id, @RequestBody Artikel artikelDetails) {
        return artikelRepository.findById(id).map(artikel -> {
            // Aktualisieren der Felder
            artikel.setName(artikelDetails.getName());
            artikel.setBestand(artikelDetails.getBestand());
            artikel.setPreis(artikelDetails.getPreis());
            Artikel aktualisiert = artikelRepository.save(artikel);
            return ResponseEntity.ok(aktualisiert);
        }).orElse(ResponseEntity.notFound().build()); // Artikel nicht gefunden
    }
}

