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
    private double preis;

    // Standardkonstruktor (für JPA erforderlich)
    public Artikel() {}
    
    // Konstruktor ohne ID (für manuelles Erstellen)
    public Artikel(String name, int bestand, double preis) {
        this.name = name;
        this.bestand = bestand;
        this.preis = preis;
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

    public double getPreis() {
        return this.preis;
    }

    public void setPreis(double preis) {
        this.preis = preis;
    }
}
