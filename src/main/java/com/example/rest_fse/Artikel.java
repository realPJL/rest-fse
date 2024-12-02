package com.example.rest_fse;

public class Artikel {
    private int id; // ID wird automatisch zugewiesen
    private String name;
    private int bestand;
    private double preis;

    // Konstruktor für Artikel (ohne ID, da die Datenbank sie vergibt)
    public Artikel(String name, int bestand, double preis) {
        if (preis < 0) {
            throw new IllegalArgumentException("Der Preis darf nicht kleiner oder gleich 0 sein.");
        }
        if (name == null) {
            throw new IllegalArgumentException("Der Artikel muss einen Namen haben.");
        }
        if (bestand < 0) {
            throw new IllegalArgumentException("Der Bestand kann nicht kleiner als 0 sein.");
        }

        this.name = name;
        this.bestand = bestand;
        this.preis = preis;
    }

    // Konstruktor für Artikel mit ID (z. B. beim Abrufen aus der Datenbank)
    public Artikel(int id, String name, int bestand, double preis) {
        this(name, bestand, preis);
        this.id = id;
    }

    public int getID() {
        return this.id;
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
