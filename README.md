
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
#### Linux (Deb/Ubuntu)
1. **Pakete aktualisieren:**
   ```bash
   sudo apt update
   ```
2. **MariaDB installieren:**
   ```bash
   sudo apt intall mariadb-server -y
   ```
3. **MariaDB starten und testen:**
   ```bash
   sudo systemctl start mariadb
   sudo systemctl enable mariadb
   ```
4. **Installation sichern (optional):**
   ```bash
   sudo mysql_secure_installation
   ```
   - set a root password
   - remove anonymous users
   - disallow root login remotely
   - remove test databases
5. **Installation verifizieren:**
   ```bash
   mysql --version
   ```

#### MacOS
1. **MariaDB mit Brew installieren:**
   ```bash
   brew install mariadb
   ```
2. **MariaDB starten:**
   ```bash
   brew services start mariadb
   ```
3. **Installation sichern (optional):**
   ```bash
   mysql_secure_installation
   ```
   - set a root password
   - remove anonymous users
   - disallow root login remotely
   - remove test databases
4. **Installation verifizieren:**
   ```bash
   mysql --version
   ```
#### Windows
TODO



1. **Repository klonen**:
   ```bash
   git clone https://github.com/realPJL/rest-fse.git
   cd rest-fse
   ```
2. **DB aufsetzen:**
   ```bash
   mysql -u root -p
   ```
   
   ```bash
   CREATE DATABASE 'your_database_name';
   ```

   ```bash
   CREATE USER 'your_user'@'%' IDENTIFIED BY 'your_password';
   ```

   ```bash
   GRANT ALL PRIVILEGES ON 'your_database_name'.* TO 'your_user'@'%';
   ```

   ```bash
   FLUSH PRIVILEGES;
   ```
```your_user``` 
Dies ist der Benutzername, der erstellt und für die Verbindung zur Datenbank verwendet wird.

```your_password``` 
Dies ist das mit dem Benutzernamen verbundene Passwort.


```your_database_name```
Dies ist der Name der Datenbank, die Ihre Anwendung verwenden wird.
Ersetzen Sie ihn durch einen beschreibenden Namen, z. B. den Namen des Projekts.

```%```
Dies ist ein Platzhalter für einen beliebigen Host. Wenn Benutzer den Zugriff auf einen bestimmten Host beschränken wollen (z. B. nur localhost oder einen bestimmten Server), sollten sie % ersetzen:
- 'localhost' für Verbindungen nur vom selben Server.
- '192.168.1.100' für Verbindungen von einer bestimmten IP-Adresse.
**ACHTUNG!**
Wenn Sie Benutzername, Passwort und DB-Name gesetzt haben, müssen sie diese in die ```application.properties``` eintragen.

3. **Abhängigkeiten bauen**:
   ```bash
   ./gradlew clean build
   ```
4. **Anwendung starten**:
   ```bash
   ./gradlew bootRun
   ```
5. **API testen**:
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
