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
    public ResponseEntity<Object> getAlleArtikel() {
        List<Artikel> artikelListe = artikelRepository.findAll();

        if (artikelListe.isEmpty()) {
            // Rückgabe einer Fehlermeldung, wenn keine Artikel vorhanden sind
            return ResponseEntity.status(404).body("(404) Keine Artikel vorhanden bzw. im Inventar.");
        }

        // Rückgabe der Artikelliste
        return ResponseEntity.ok(artikelListe);
    }




    // GET: Einen Artikel nach ID abrufen (Invoke-RestMethod -Uri "http://localhost:8080/api/artikel/"id""))
    @GetMapping("/{id}")
    public ResponseEntity<Artikel> getArtikelById(@PathVariable Long id) {
        return artikelRepository.findById(id)
                .map(artikel -> ResponseEntity.ok(artikel)) // Artikel gefunden
                .orElse(ResponseEntity.notFound().build()); // Artikel nicht gefunden
    }




    // POST: Neuen Artikel hinzufügen (Invoke-RestMethod -Uri "http://localhost:8080/api/artikel" -Method Post -ContentType "application/json" -Body '{"name": "", "bestand": , "preis": }')
    @PostMapping
    public ResponseEntity<Object> erstelleArtikel(@RequestBody Artikel neuerArtikel) {
        // Validierung
        if (neuerArtikel.getBestand() < 0 || neuerArtikel.getBestand() > 1000000 || neuerArtikel.getPreis() < 0 || neuerArtikel.getPreis() > 1000000 || neuerArtikel.getName() == null || neuerArtikel.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("(400) Ungültige Eingabe: Bestand, Preis oder Name haben einen nicht gültigen Wert."); // Ungültige Eingabe
        }
        Artikel gespeicherterArtikel = artikelRepository.save(neuerArtikel);
        return ResponseEntity.status(201).body(gespeicherterArtikel);
    }




    // PUT: Artikel aktualisieren (Invoke-RestMethod -Uri "http://localhost:8080/api/artikel/"id"" -Method Put -ContentType "application/json" -Body '{"name": "", "bestand": , "preis": }')
    @PutMapping("/{id}")
    public ResponseEntity<Artikel> aktualisiereArtikel(@PathVariable Long id, @RequestBody Artikel artikelDetails) {
        
        // Validierung
        if (artikelDetails.getBestand() < 0 || artikelDetails.getBestand() > 1000000 || artikelDetails.getPreis() < 0 || artikelDetails.getPreis() > 1000000 || artikelDetails.getName() == null || artikelDetails.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().build(); // Ungültige Eingabe
        }
            // ID finden
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
        public ResponseEntity<String> deleteArtikel(@PathVariable Long id) {
        if (!artikelRepository.existsById(id)) {
            return ResponseEntity.status(404).body("(404) Artikel mit ID " + id + " wurde nicht gefunden.");
        }

        artikelRepository.deleteById(id);
        return ResponseEntity.ok("Artikel mit ID " + id + " wurde erfolgreich gelöscht.");
    }
}

