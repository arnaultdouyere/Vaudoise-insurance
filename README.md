# 🏦 Vaudoise Insurance — Backend API

Application **Spring Boot 3.5 / Java 21** permettant de gérer les **clients** (particuliers ou entreprises) et leurs **contrats** d’assurance.

Elle expose une API REST documentée avec **Swagger**, persiste ses données en **H2** (par défaut) ou **MySQL**, et comprend une suite complète de **tests unitaires** et **d’intégration** (JUnit 5 + Mockito).

---

## 🚀 Sommaire

- [🏦 Vaudoise Insurance — Backend API](#-vaudoise-insurance--backend-api)
  - [🚀 Sommaire](#-sommaire)
  - [⚙️ Technologies](#️-technologies)
  - [🧩 Structure du projet](#-structure-du-projet)
  - [💻 Prérequis](#-prérequis)
  - [🟢 Installation \& lancement](#-installation--lancement)
    - [Démarrage rapide (profil H2 par défaut)](#démarrage-rapide-profil-h2-par-défaut)
  - [🧱 Profils \& bases de données](#-profils--bases-de-données)
  - [▶️ Profil par défaut : h2](#️-profil-par-défaut--h2)
  - [📘 JDBC H2](#-jdbc-h2)
  - [▶️ Profil mysql](#️-profil-mysql)
  - [📘 JDBC MySQL](#-jdbc-mysql)
  - [▶️ Profil test](#️-profil-test)
  - [🌍 URLs utiles](#-urls-utiles)
  - [📘 Swagger / OpenAPI](#-swagger--openapi)
    - [Clients](#clients)
    - [Contrats](#contrats)
  - [🧮 Console H2](#-console-h2)
  - [🧾 Schéma \& règles métier](#-schéma--règles-métier)
    - [🧍 Client](#-client)
    - [📄 Contract](#-contract)
  - [✅ Tests \& couverture](#-tests--couverture)
  - [🧰 Commandes Maven](#-commandes-maven)
  - [💬 Exemples cURL](#-exemples-curl)
  - [🧩 Dépannage](#-dépannage)
  - [🧭 Améliorations futures](#-améliorations-futures)
  - [🧠 Résumé architecture](#-résumé-architecture)

---

## ⚙️ Technologies

| Catégorie | Stack |
|------------|--------|
| Langage | Java 21 |
| Framework | Spring Boot 3.5.6 |
| ORM | Spring Data JPA (Hibernate) |
| DB par défaut | H2 persistante (`./.vaudoise/db`) |
| DB alternative | MySQL 8+ |
| Documentation | Springdoc / Swagger UI |
| Build | Maven Wrapper (`mvnw`) |
| Tests | JUnit 5, Mockito, MockMvc |
| Outils | Lombok, DevTools |

---

## 🧩 Structure du projet

```
src/
├── main/
│ ├── java/com/vaudoise/
│ │ ├── model/ # Entités JPA
│ │ ├── dto/ # DTO Request/Response
│ │ ├── repository/ # Repositories Spring Data
│ │ ├── service/ # Services métier
│ │ ├── controller/ # REST Controllers + GlobalExceptionHandler
│ │ └── VaudoiseInsuranceAppApplication.java
│ └── resources/
│ │ ├── application.properties # Profil principal
│ │ ├── application-h2.properties # H2 (par défaut)
│ │ ├── application-mysql.properties # MySQL (optionnel)
│ │ ├── application-test.properties # H2 mémoire pour tests
│ └── static/templates (si ajout UI)
└── test/java/com/vaudoise/
│ │ ├── controller/ # Tests MockMvc (Controller)
│ │ ├── service/ # Tests unitaires Mockito
│ │ ├── repository/ # Tests JPA
│ │ └── others
```
---

## 💻 Prérequis

- Java 21  
- Maven 3.9+  
- Port 8080 libre  
- (Optionnel) MySQL local si tu veux utiliser ce profil

---

## 🟢 Installation & lancement

### Démarrage rapide (profil H2 par défaut)
```bash
# Compilation + build
./mvnw clean package

# Lancement (profil h2 activé par défaut)
./mvnw spring-boot:run
# ou
java -jar target/vaudoise-0.0.1-SNAPSHOT.jar
```
## 🧱 Profils & bases de données
## ▶️ Profil par défaut : h2

Fichier : application-h2.properties

Base persistante sur disque : ./.vaudoise/db.mv.db

Console disponible via /h2

## 📘 JDBC H2

```text
jdbc:h2:file:./.vaudoise/db;MODE=MySQL;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1
user=sa
password=
```

## ▶️ Profil mysql

Fichier : application-mysql.properties

Base : vaudoise_insurance_db

Création manuelle si nécessaire :

```sql
CREATE DATABASE vaudoise_insurance_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

## 📘 JDBC MySQL
```text
jdbc:mysql://localhost:3306/vaudoise_insurance_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
user=root
password=(ton mot de passe)
```

Lancement :
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=mysql
```
## ▶️ Profil test

Utilisé automatiquement pendant mvn test

H2 en mémoire (jdbc:h2:mem:testdb)

Aucun changement manuel requis

## 🌍 URLs utiles
| Type | URL |
|------------|--------|
API Swagger UI	|http://localhost:8080/swagger-ui.|html
Spécification JSON	| http://localhost:8080/v3/|api-docs
Console H2|	http://localhost:8080/h2

## 📘 Swagger / OpenAPI

Tous les endpoints REST sont documentés via springdoc-openapi :
### Clients
| Méthode | Endpoint | Description |
|---------|--------|--------|
POST	|/api/clients	|Créer un client|
GET	|/api/clients	|Lister tous les clients|
GET	|/api/clients/{id}	|Détails d’un client|
PUT	|/api/clients/{id}	|Mettre à jour un client|
DELETE	|/api/clients/{id}	|Supprimer un client (clôture ses contrats)|

### Contrats
| Méthode | Endpoint | Description |
|---------|--------|--------|
POST	|/api/clients/{clientId}/contracts	|Créer un contrat|
PATCH	|/api/contracts/{id}/amount	|Modifier le montant|
GET	|/api/clients/{clientId}/contracts	|Lister les contrats actifs (filtres date update)|
GET	|/api/clients/{clientId}/contracts/active/sum|	Somme des contrats actifs

## 🧮 Console H2

Accessible à :
👉 http://localhost:8080/h2

Configuration :
```text
Driver Class: org.h2.Driver
JDBC URL: jdbc:h2:file:./.vaudoise/db
User Name: sa
Password: 
```

⚠️ Ne pas utiliser AUTO_SERVER=TRUE ni DB_CLOSE_ON_EXIT=FALSE (non supportés par H2 2.x)

## 🧾 Schéma & règles métier
### 🧍 Client
| Champ | Type |Règles |
|------------|--------|--------|
id	|Long|	PK
clientType	|Enum (PERSON / COMPANY)	|obligatoire
name	|String	|obligatoire
email	|String	|format valide
phone	|String	|non vide
birthDate	|LocalDate	|PERSON uniquement, immuable
companyIdentifier	|String	|COMPANY uniquement, immuable
createdAt	|OffsetDateTime	|automatique
updatedAt	|OffsetDateTime|	automatique

Suppression client : met endDate = today sur tous ses contrats actifs.

### 📄 Contract
| Champ | Type |Règles |
|------------|--------|--------|
id	|Long	|PK
client	|FK Client	|obligatoire
startDate	|LocalDate	|défaut = today
endDate	|LocalDate	|null = illimité
costAmount	|BigDecimal|	>= 0
updateDate	|OffsetDateTime	|auto lors d’un PATCH amount

Actif = endDate is null ou endDate > today.

## ✅ Tests & couverture

Les tests couvrent :

Unitaires : services (Mockito)

Intégration : controllers (MockMvc)

JPA : requêtes et mapping

End-to-End : suppression client ferme ses contrats

Lancer :
```bash
./mvnw clean test
```

Rapport couverture (si Jacoco ajouté) :
→ target/site/jacoco/index.html

## 🧰 Commandes Maven
	
| Commande | Description |
|------------|--------|
./mvnw clean	|Nettoie le projet
./mvnw test	|Exécute les tests (profil test)
./mvnw package	|Build le jar exécutable
./mvnw spring-boot:run	|Lance l’application
./mvnw verify	|Compile + tests + check intégrité

## 💬 Exemples cURL

Créer un client :
```bash
curl -X POST http://localhost:8080/api/clients \
  -H "Content-Type: application/json" \
  -d '{"type":"PERSON","name":"Alice","email":"alice@ex.com","phone":"+33 6 11 22 33 44","birthDate":"1990-01-01"}'
```

Créer un contrat :
```bash
curl -X POST http://localhost:8080/api/clients/1/contracts \
  -H "Content-Type: application/json" \
  -d '{"costAmount":100.50,"endDate":"2099-12-31"}'
```

Somme contrats actifs :
```bash
curl http://localhost:8080/api/clients/1/contracts/active/sum
```
## 🧩 Dépannage
| Problème | Solution |
|------------|--------|
❌ Feature not supported: AUTO_SERVER=TRUE	|Supprimer ce flag dans les .properties
❌ Communications link failure (MySQL)	|Vérifie que ton serveur MySQL tourne sur le port 3306
❌ swagger-ui.html introuvable	|Vérifie la dépendance springdoc-openapi-starter-webmvc-ui
❌ Erreur de schéma	|Supprime .vaudoise/db.mv.db et relance (clean regénère)
❌ Accès H2 refusé	|Vérifie spring.h2.console.enabled=true et l’URL JDBC exacte

## 🧭 Améliorations futures

 Ajout de Flyway pour versionner le schéma
 Mise en place de Jacoco + GitHub Actions CI
 Ajout de Spring Security + JWT
 Pagination sur /api/clients
 Metrics / Actuator / Health-checks
 Passage à Testcontainers pour les tests MySQL
 Gestion git : Étant seul sur un projet de test, je me suis permis des commandes Git que je ne ferai pas sur un projet collaboratif.

## 🧠 Résumé architecture
L’application repose sur une architecture clean & modulaire.
Les entités JPA Client et Contract modélisent le domaine, avec des relations fortes (@OneToMany).
Les repositories gèrent les requêtes (y compris les filtres updatedBetween et la somme des contrats actifs).
Les services implémentent la logique métier : validation, immutabilité, calcul des montants et clôture automatique des contrats lors de la suppression d’un client.
Les controllers REST exposent les endpoints JSON documentés via Swagger.
Les profils Spring permettent de basculer entre H2 persistante, H2 mémoire, et MySQL sans modifier le code.
Des tests complets garantissent la fiabilité du domaine, des services et des intégrations REST.
Une console H2 intégrée simplifie l’inspection locale des données.

Voici une version plus claire :
- Entities (model) : Client, Contract
- Repositories : gestion JPA
- Services : logique métier
- Controllers REST : exposition HTTP
- Swagger : documentation automatique
- Profiles : H2 (persistante), MySQL, test
- Tests : unitaires + intégration
- Preuve : Swagger + tests + console H2 démontrent le bon fonctionnement complet de l’API.

**Author : Arnault Douyere**