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

@RestController
@RequestMapping("/api/artikel")
public class ArtikelController {

    @Autowired
    private ArtikelRepository artikelRepository;

    // GET: Alle Artikel abrufen (Invoke-RestMethod -Uri "http://localhost:8080/api/artikel")
    @GetMapping
    public List<Artikel> getAlleArtikel() {
        return artikelRepository.findAll(); // Holt alle Artikel aus der Datenbank
    }

    // GET: Einen Artikel nach ID abrufen (Invoke-RestMethod -Uri "http://localhost:8080/api/artikel/"id""))
    @GetMapping("/{id}")
    public ResponseEntity<Artikel> getArtikelById(@PathVariable Long id) {
        return artikelRepository.findById(id)
                .map(ResponseEntity::ok) // Artikel gefunden
                .orElse(ResponseEntity.notFound().build()); // Artikel nicht gefunden
    }


    // POST: Neuen Artikel hinzufügen (Invoke-RestMethod -Uri "http://localhost:8080/api/artikel" -Method Post -ContentType "application/json" -Body '{"name": "", "bestand": , "preis": }')
    @PostMapping
    public ResponseEntity<Artikel> erstelleArtikel(@RequestBody Artikel neuerArtikel) {
        // Validierung
        if (neuerArtikel.getBestand() < 0 || neuerArtikel.getPreis() < 0) {
            return ResponseEntity.badRequest().build(); // Ungültige Eingabe
        }
        Artikel gespeicherterArtikel = artikelRepository.save(neuerArtikel);
        return ResponseEntity.status(201).body(gespeicherterArtikel);
    }

    // PUT: Artikel aktualisieren (Invoke-RestMethod -Uri "http://localhost:8080/api/artikel/"id"" -Method Put -ContentType "application/json" -Body '{"name": "", "bestand": , "preis": }')
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

    //DELETE: Artikel löschen (Invoke-RestMethod -Uri "http://localhost:8080/api/artikel/"id"" -Method Delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtikel(@PathVariable Long id) {
        artikelRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // Statuscode 204 signalisiert, dass die Anfrage erfolgreich war, aber die Antwort keinen Inhalt hat
    }
}

