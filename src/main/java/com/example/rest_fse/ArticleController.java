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
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;

    // GET: Alle Artikel abrufen
    @GetMapping
    public ResponseEntity<Object> getAllArticle() {
        List<Article> artikelListe = articleRepository.findAll();
        if (artikelListe.isEmpty()) {
            // Rückgabe einer Fehlermeldung, wenn keine Artikel gefunden wurden
            throw new EmptyResultDataAccessException("Keine Artikel gvorhanden bzw. im Inventar", 1);
        }
        // Rückgabe der Artikelliste
        return ResponseEntity.ok(artikelListe);
    }

    // GET: Einen Artikel nach ID abrufen
    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
        // Validierung der ID
        if (id <= 0) {
            throw new IllegalArgumentException("Die Artikel-ID muss größer als 0 sein.");
        }
        return articleRepository.findById(id)
                .map(ResponseEntity::ok) // Artikel gefunden
                .orElseThrow(() -> new EmptyResultDataAccessException("Artikel mit der ID " + id + " nicht gefunden.", 1)); // Artikel nicht gefunden
    }

    // GET: Artikel basierend auf Lagerort abrufen
    @GetMapping("/lagerort")
    public ResponseEntity<List<Article>> getArtikelByLagerort(@RequestParam String lagerort) {
        if (lagerort == null || lagerort.trim().isEmpty()) {
            throw new IllegalArgumentException("Lagerort darf nicht leer sein.");
        }
        if (!lagerort.matches("^[a-zA-Z\s]+$")) {
            throw new IllegalArgumentException("Lagerort darf nur Buchstaben enthalten.");
        }
        if (!articleRepository.existsByLagerort(lagerort)) {
            throw new EmptyResultDataAccessException("Lagerort " + lagerort + " nicht gefunden.", 1);
        }

        List<Article> articleList = articleRepository.findByLagerort(lagerort);
        if (articleList.isEmpty()) {
            throw new EmptyResultDataAccessException("Keine Artikel im Lagerort " + lagerort + " vorhanden.", 1);
        }
        return ResponseEntity.ok(articleList);
    }

    // POST: Neuen Artikel hinzufügen
    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody Article newArticle) {
        // Validierung der Eingabedaten
        if (newArticle.getBestand() < 0 || newArticle.getBestand() > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Bestand hat einen ungültigen Wert als Eingabe.");
        }
        if (newArticle.getPreis() < 0 || newArticle.getPreis() > 1000000) {
            throw new IllegalArgumentException("Preis hat einen ungültigen Wert als Eingabe.");
        }
        if (newArticle.getName().trim().isEmpty() || newArticle.getName() == null) {
            throw new IllegalArgumentException("Name darf nicht leer sein.");
        }
        if (!newArticle.getName().matches("^[a-zA-Z\s]+$")) {
            throw new IllegalArgumentException("Name darf nur Buchstaben und Leerzeichen enthalten.");
        }
        if (newArticle.getLagerort().trim().isEmpty() || newArticle.getLagerort() == null) {
            throw new IllegalArgumentException("Lagerort darf nicht leer sein.");
        }
        if (!newArticle.getLagerort().matches("^[a-zA-Z\s]+$")) {
            throw new IllegalArgumentException("Lagerort darf nur Buchstaben enthalten.");
        }

        // Artikel speichern und ausgeben
        Article savedArticle = articleRepository.save(newArticle);
        return ResponseEntity.status(201).body(savedArticle);
    }

    // PUT: Artikel aktualisieren
    @PutMapping("/{id}")
    public ResponseEntity<Article> aktualisiereArtikel(@PathVariable Long id, @RequestBody Article articleDetails) {
        if (id <= 0) {
            throw new IllegalArgumentException("Die Artikel-ID muss größer als 0 sein.");
        }
        
        // Eingabedaten validieren
        if (articleDetails.getBestand() < 0 || articleDetails.getBestand() > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Bestand hat einen ungültigen Wert als Eingabe.");
        }
        if (articleDetails.getPreis() < 0 || articleDetails.getPreis() > 1000000) {
            throw new IllegalArgumentException("Preis hat einen ungültigen Wert als Eingabe.");
        }
        if (articleDetails.getName().trim().isEmpty() || articleDetails.getName() == null) {
            throw new IllegalArgumentException("Name darf nicht leer sein.");
        }
        if (!articleDetails.getName().matches("^[a-zA-Z\s]+$")) {
            throw new IllegalArgumentException("Name darf nur Buchstaben.");
        }
        if (articleDetails.getLagerort().trim().isEmpty() || articleDetails.getLagerort() == null) {
            throw new IllegalArgumentException("Lagerort darf nicht leer sein.");
        }
        if (!articleDetails.getLagerort().matches("^[a-zA-Z\s]+$")) {
            throw new IllegalArgumentException("Lagerort darf nur Buchstaben enthalten.");
        }

        return articleRepository.findById(id).map(article -> {
            article.setName(articleDetails.getName());
            article.setBestand(articleDetails.getBestand());
            article.setPreis(articleDetails.getPreis());
            Article aktualisiert = articleRepository.save(article);
            return ResponseEntity.ok(aktualisiert);
        }).orElseThrow(() -> new EmptyResultDataAccessException("Artikel mit der ID " + id + " nicht gefunden.", 1)); // Artikel gefunden
    }

    // DELETE: Artikel löschen
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteArticle(@PathVariable Long id) {
        // Validierung der ID
        if (id <= 0) {
            throw new IllegalArgumentException("Artikel-ID muss größer als 0 sein.");
        }
        if (!articleRepository.existsById(id)) {
            throw new EmptyResultDataAccessException("Artikel mit der ID " + id + " nicht gefunden.", 1);
        }

        articleRepository.deleteById(id);
        return ResponseEntity.ok("Artikel mit der ID " + id + " gelöscht.");
    }
}
