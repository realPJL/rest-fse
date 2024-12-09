package com.example.rest_fse;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Artikel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private int bestand;
    private double einzelpreis;
    private String lagerort;

    // Standardkonstruktor (für JPA erforderlich)
    public Artikel() {}
    
    // Konstruktor ohne ID (für manuelles Erstellen)
    public Artikel(String name, int bestand, double einzelpreis, String lagerort) {
        this.name = name;
        this.bestand = bestand;
        this.einzelpreis = einzelpreis;
        this.lagerort = lagerort;
    }

    // Getter und Setter
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBestand() {
        return this.bestand;
    }

    public void setBestand(int bestand) {
        this.bestand = bestand;
    }

    public double getEinzelpreis() {
        return this.einzelpreis;
    }

    public void setEinzelpreis(double einzelpreis) {
        this.einzelpreis = einzelpreis;
    }

    public String getLagerort() {
        return this.lagerort;
    }

    public void setLagerort(String lagerort) {
        this.lagerort = lagerort;
    }
}
