
# REST-FSE Projekt

## Projektbeschreibung

Dieses Projekt implementiert ein Backend einer RESTful-Webanwendung zur Verwaltung von Artikeln. Die Anwendung bietet Endpunkte für CRUD-Operationen (Erstellen, Abrufen, Aktualisieren und Löschen von Artikeln) und verfügt über Funktionsweisen zur Fehlerbehandlung.

### Hauptfunktionen:
1. **Artikelverwaltung**: Hinzufügen, Abrufen, Aktualisieren und Löschen von Artikeln.
2. **Filterung nach Lagerort**: Artikel können nach Lagerort abgefragt werden.
3. **Dynamische Portzuweisung**: Der Server sucht automatisch einen verfügbaren Port, beginnend bei Port 8080. Falls dieser belegt ist, wird autoinkrementiert.
4. **Globale Fehlerbehandlung**: Einheitliche Behandlung von Ausnahmen wie ungültigen Anfragen oder nicht gefundenen Ressourcen.

---

## Projektstruktur

- **`ApiError`**: Modelliert Fehlerdetails für API-Antworten.
- **`Artikel`**: Entität, die Artikel-Daten in der Datenbank speichert.
- **`ArtikelRepository`**: Datenzugriffsschicht mit Standard- und benutzerdefinierten Methoden.
- **`ArtikelController`**: REST-Controller für die Artikel-API-Endpunkte.
- **`GlobalExceptionHandler`**: Behandelt Fehler global und gibt passende HTTP-Statuscodes und Fehlermeldungen zurück.
- **`PortUtil`**: Findet dynamisch einen verfügbaren Port für den Server.
- **`RestFseApplication`**: Haupteinstiegspunkt der Spring-Boot-Anwendung.

---

## Installationsanleitung

### Voraussetzungen:
1. **Java 17** oder neuer.
2. **Gradle** für den Build-Prozess.
3. **Spring Boot** als Framework
4. **MariaDB** für das persistieren der Daten.
5. **git**

### Schritte:
1. **Repository klonen**:
   ```bash
   git clone https://github.com/realPJL/rest-fse.git
   cd rest-fse
   ```
2. **Abhängigkeiten bauen**:
   ```bash
   ./gradlew clean build
   ```
3. **Anwendung starten**:
   ```bash
   ./gradlew bootRun
   ```
4. **API testen**:
   Die Anwendung läuft auf einem dynamisch gewählten Port. Der Port wird nach dem Start in der Konsole ausgegeben. Standardmäßig läuft die Anwendung auf Port 8080, falls dieser Belegt ist wird automatisch auf den nächst höheren Port gewechselt.

---

## Abhängigkeiten

- **Spring Boot Starter Web**: Bereitstellung der REST-Funktionalität.
- **Spring Boot Starter Data JPA**: Datenbankzugriff.
- **MariaDB**: Eingebettete Datenbank für Tests.
- **Jakarta Persistence API (JPA)**: Für die Objekt-Datenbank-Mapping.
- **Java Time API**: Verarbeitung von Datums- und Zeitstempeln.

---

## API-Endpunkte

### Artikel-Management:
- **GET** `/api/artikel`: Alle Artikel abrufen.
- **GET** `/api/artikel/{id}`: Artikel mit einer bestimmten ID abrufen.
- **GET** `/api/artikel/lagerort`: Artikel nach Lagerort filtern (Query-Parameter: `lagerort`).
- **POST** `/api/artikel`: Neuen Artikel erstellen.
- **PUT** `/api/artikel/{id}`: Artikel aktualisieren.
- **DELETE** `/api/artikel/{id}`: Artikel löschen.

---

## Fehlerbehandlung

Die Anwendung behandelt folgende Fehler:
- **Ungültige Anfragen**: HTTP 400 (Bad Request)
- **Nicht gefundene Ressourcen**: HTTP 404 (Not Found)
- **Serverfehler**: HTTP 500 (Internal Server Error)
