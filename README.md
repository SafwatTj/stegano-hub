🔒 STEGHUB — LSB-Steganographie REST-API

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)
[![JWT](https://img.shields.io/badge/JWT-Gesichert-orange.svg)](https://jwt.io/)
[![Prometheus](https://img.shields.io/badge/Metriken-Prometheus-red.svg)](https://prometheus.io/)
[![H2](https://img.shields.io/badge/Datenbank-H2-yellow.svg)](https://www.h2database.com/)

> **Professionelle LSB-Steganographie REST-API** mit JWT-Authentifizierung, Docker-Containerisierung, Prometheus-Metriken und H2-Datenbank für Operations-History.

---

## 🎯 Überblick

**STEGHUB** ist eine moderne REST-API zur verdeckten Datenübertragung mittels **LSB (Least Significant Bit)** Steganographie in PNG-Bildern.

### Hauptmerkmale

| Merkmal | Beschreibung |
|---------|---------------|
| **LSB Steganographie** | Verstecken von Nachrichten in RGB-Kanälen von PNG-Bildern |
| **JWT Authentifizierung** | Sichere API mit Bearer-Token |
| **Asynchrone Verarbeitung** | Non-blocking Encoding/Decoding für große Dateien |
| **Docker Ready** | Fertiger Container für einfache Bereitstellung |
| **Prometheus Metriken** | Überwachung von Encode/Decode-Operationen |
| **H2 Datenbank** | Vollständige Historie aller Operationen |
| **Modernes Web-UI** | Dark-Theme Interface für einfache Tests |

---

## 🚀 Technologie-Stack

- **Java 17**
- **Spring Boot 3.1.5**
- **Spring Security** mit JWT
- **Spring Data JPA** (H2)
- **Micrometer + Prometheus**
- **Docker**
- **HTML5/CSS3/JavaScript**

---

## 📦 Installation & Start

### Lokal mit Maven

```bash
git clone https://github.com/SafwatTj/stegano-hub.git
cd stegano-hub
mvn clean package -DskipTests
java -jar target/stegano-hub-0.0.1-SNAPSHOT.jar
Mit Docker
bash
docker build -t stegano-hub:latest .
docker run -d -p 8080:8080 --name stegano-hub stegano-hub:latest
🔑 API-Endpoints
Authentifizierung
http
POST /authenticate
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
Steganographie
Methode	Endpoint	Beschreibung
POST	/api/stegano/encode	Nachricht in PNG verstecken
POST	/api/stegano/decode	Nachricht aus PNG extrahieren
GET	/api/stegano/history	Operations-History anzeigen
Asynchrone API
Methode	Endpoint	Beschreibung
POST	/api/async/stegano/encode	Asynchron verstecken
POST	/api/async/stegano/decode	Asynchron auslesen
📊 Monitoring & Metriken
Endpoint	Beschreibung
/actuator/health	Health-Check
/actuator/metrics	Alle Metriken
/actuator/prometheus	Prometheus-Format
Benutzerdefinierte Metriken:

stegano.encode.total — Anzahl Encode-Operationen

stegano.decode.total — Anzahl Decode-Operationen

stegano.errors.total — Anzahl Fehler

🗄️ H2-Datenbank
Konsole: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:stegano_db

Username: sa

Password: (leer)

Tabelle OPERATIONS:

id, type, username, messageLength, imageName, timestamp, status

🎨 Web-Interface
Nach dem Start öffnen: http://localhost:8080

Dark Theme mit Terminal-Ästhetik

ENCODE: PNG + Nachricht → verschlüsseltes PNG

DECODE: PNG hochladen → Nachricht anzeigen

🔐 Sicherheit
JWT mit HS256-Signatur

BCrypt für Passwort-Hashing

Stateless Sessions

CORS deaktiviert

📁 Projektstruktur
text
src/
├── main/java/com/steganohub/
│   ├── controller/          # REST-Endpoints
│   ├── service/             # LSB-Logik, Async, Metriken
│   ├── repository/          # JPA-Repository
│   ├── model/               # Operation-Entity
│   ├── security/            # JWT, SecurityConfig
│   └── SteganoHubApplication.java
├── main/resources/
│   ├── static/index.html    # Web-UI
│   └── application.properties
└── test/                    # Unit-Tests
🧪 Tests ausführen
bash
mvn test
📈 Limits
Maximale Nachrichtengröße: 1000 Zeichen

Maximale Dateigröße: 10 MB

Unterstützte Formate: PNG (mit Alphakanal)

UTF-8 Support: Deutsch, Russisch, Persisch

📄 Lizenz
MIT License — Freie Nutzung, Modifikation und Verteilung.

👨‍💻 Entwickler
Safwat Burkhonov
Java Fullstack Developer | 3D-Visualisierung | Kryptographie

GitHub: github.com/SafwatTj

LinkedIn: linkedin.com/in/safwat-burkhonov-java-developer

E-Mail: safwat1@web.de

⭐ Support
Wenn dir dieses Projekt gefällt, gib bitte einen Star auf GitHub!

📚 Verwandte Projekte
Steganography Suite — 7-in-1 Steganographie-Tool mit GUI

Unternehmens-KI-Chat — Lokale KI-Plattform mit Docker + Open WebUI

STEGHUB — Professionelle Steganographie für Entwickler und Sicherheitsexperten. 🔒
