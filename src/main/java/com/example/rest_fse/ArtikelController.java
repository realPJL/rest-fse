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

    // GET: Alle Artikel abrufen
    @GetMapping
    public ResponseEntity<Object> getAlleArtikel() {
        List<Artikel> artikelListe = artikelRepository.findAll();

        if (artikelListe.isEmpty()) {
            // Rückgabe einer Fehlermeldung, wenn keine Artikel vorhanden sind
            return ResponseEntity.status(404).body("Status:(404) Keine Artikel vorhanden bzw. im Inventar.");
        }

        // Rückgabe der Artikelliste
        return ResponseEntity.ok(artikelListe);
    }



    // GET: einzelnen Artikel mit ID abrufen
    @GetMapping("/{id}")
    public ResponseEntity<Object> getArtikelById(@PathVariable Long id) {
        if (id <= 0) {
            return ResponseEntity.status(400).body("Status:(400) Ungültige Anfrage: Die Artikel-ID muss größer als 0 sein.");
        }

        return artikelRepository.findById(id)
                .map(artikel -> ResponseEntity.ok((Object) artikel)) // Artikel gefunden
                .orElse(ResponseEntity.status(404).body("Status:(404) Artikel mit ID " + id + " wurde nicht gefunden.")); // Artikel nicht gefunden
    }



    // POST: Neuen Artikel hinzufügen
    @PostMapping
    public ResponseEntity<Object> erstelleArtikel(@RequestBody Artikel neuerArtikel) {
        // Validierung
        if (neuerArtikel.getBestand() < 0 || neuerArtikel.getBestand() > 1000000) {
            return ResponseEntity.status(400).body("Status:(400) Ungültige Eingabe: Bestand hat einen nicht gültigen Wert als Eingabe."); // Bestand Ungültige Eingabe
        }
        if (neuerArtikel.getPreis() < 0 || neuerArtikel.getPreis() > 1000000) {
            return ResponseEntity.status(400).body("Status:(400) Ungültige Eingabe: Preis hat einen nicht gültigen Wert als Eingabe."); // Preis Ungültige Eingabe
        }
        if (neuerArtikel.getName() == null || neuerArtikel.getName().trim().isEmpty()) {
           return ResponseEntity.status(400).body("Status:(400) Ungültige Eingabe: Name hat einen nicht gültigen Wert als Eingabe."); // Ungültige Eingabe
        } 
        
        Artikel gespeicherterArtikel = artikelRepository.save(neuerArtikel);
        return ResponseEntity.status(201).body(gespeicherterArtikel);
    }



    // PUT: Artikel aktualisieren
    @PutMapping("/{id}")
    public ResponseEntity<Object> aktualisiereArtikel(@PathVariable Long id, @RequestBody Artikel artikelDetails) {
        // Validierung der ID
        if (id <= 0) {
            return ResponseEntity.status(400).body("Status:(400) Ungültige Anfrage: Die Artikel-ID muss größer als 0 sein.");
        }

        // Validierung der Eingabedaten
        if (artikelDetails.getBestand() < 0 || artikelDetails.getBestand() > 1000000) {
            return ResponseEntity.status(400).body("Status:(400) Ungültige Eingabe: Bestand hat einen nicht gültigen Wert als Eingabe."); // Bestand ungültig
        }
        if (artikelDetails.getPreis() < 0 || artikelDetails.getPreis() > 1000000) {
            return ResponseEntity.status(400).body("Status:(400) Ungültige Eingabe: Preis hat einen nicht gültigen Wert als Eingabe."); // Preis ungültig
        }
        if (artikelDetails.getName() == null || artikelDetails.getName().trim().isEmpty()) {
            return ResponseEntity.status(400).body("Status:(400) Ungültige Eingabe: Name hat einen nicht gültigen Wert als Eingabe."); // Name ungültig
        }

        // Artikel suchen und aktualisieren
        return artikelRepository.findById(id)
                .map(artikel -> {
                    artikel.setName(artikelDetails.getName());
                    artikel.setBestand(artikelDetails.getBestand());
                    artikel.setPreis(artikelDetails.getPreis());
                    Artikel aktualisiert = artikelRepository.save(artikel);
                    return ResponseEntity.ok((Object) aktualisiert);
                })
                .orElse(ResponseEntity.status(404).body("Status:(404) Artikel mit ID " + id + " wurde nicht gefunden.")); // Artikel nicht gefunden
    }


    
    //DELETE: Artikel löschen
    @DeleteMapping("/{id}")
        public ResponseEntity<String> deleteArtikel(@PathVariable Long id) {
        if (!artikelRepository.existsById(id)) {
            return ResponseEntity.status(404).body("Status:(404) Artikel mit ID " + id + " wurde nicht gefunden.");
        }

        artikelRepository.deleteById(id);
        return ResponseEntity.ok("Artikel mit ID " + id + " wurde erfolgreich gelöscht.");
    }
}