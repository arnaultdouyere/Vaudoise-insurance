🏭 API Factory – Backend Java Spring Boot

Une API RESTful permettant à des conseillers d’assurance de gérer leurs clients (personnes physiques ou morales) et leurs contrats.
Le backend est développé en Java 21 + Spring Boot 3, avec MySQL pour la persistance, et suit une architecture claire à trois couches :
Controller → Service (interface + impl) → Repository.

🚀 Lancement du projet
1️⃣ Prérequis
-Java 21+
-Maven 3.9+
-Postman (facultatif pour tester facilement l’API)

2️⃣ Installation et lancement
# 1. Démarrer la base de données + phpMyAdmin
XAMPP en local, lancement de Apache et PhpMyAdmin

# 2. Démarrer l’application Spring Boot
./mvnw spring-boot:run

3️⃣ Accès
Outil	URL
API principale	http://localhost:8080
phpMyAdmin	http://localhost:8081
Base de données	MySQL → jdbc:mysql://localhost:3306/apifactory
Utilisateur	apifactory / apifactory

🧩 Stack technique
Couche	Technologie
Langage	Java 21
Framework	Spring Boot 3.3
ORM	Hibernate / JPA
Base de données	MySQL 8
Migration DB	Flyway
Validation	Jakarta Bean Validation
Build	Maven
Tests	(optionnel) JUnit / Testcontainers
UI DB	phpMyAdmin (port 8081)

## 📜 Architecture du projet

```text
src/
├── main/
│   ├── java/com/example/apifactory/
│   │   ├── config/              → Configuration (Jackson, CORS…)
│   │   ├── domain/              → Entités JPA (Client, Contract)
│   │   ├── dto/                 → Objets de transfert (CreateRequest, Response…)
│   │   ├── repository/          → Interfaces Spring Data JPA
│   │   ├── service/             → Interfaces métier
│   │   │   └── impl/            → Implémentations concrètes
│   │   ├── validation/          → Validations custom (PERSON vs COMPANY)
│   │   └── Controller/                 → REST Controllers + gestion globale des erreurs
│   └── resources/
│       ├── application.properties
│       └── db/migration/        → Scripts Flyway (ex: init_db.sql)
└── test/
    └── java/...                 → (à compléter : tests unitaires / intégration)
```

📚 Endpoints (Swagger-like)
🔹 Clients

Méthode	Endpoint	Description
POST	/api/clients	Crée un client (type PERSON ou COMPANY).
GET	/api/clients	Retourne tous les clients.
GET	/api/clients/{id}	Retourne les infos complètes d’un client.
PUT	/api/clients/{id}	Met à jour un client (tous les champs sauf birthDate & companyIdentifier).
DELETE	/api/clients/{id}	Supprime un client et clôture ses contrats actifs à la date du jour.
🧾 Exemple – Créer un client PERSON
POST /api/clients
```text
{
  "type": "PERSON",
  "name": "Alice Dupont",
  "email": "alice@example.com",
  "phone": "+33 6 12 34 56 78",
  "birthDate": "1995-04-12"
}
```

🔹 Contracts
Méthode	Endpoint	Description
POST	/api/clients/{clientId}/contracts	Crée un contrat pour un client (startDate par défaut = date du jour).
PATCH	/api/contracts/{contractId}/amount	Met à jour le montant d’un contrat (actualise aussi updateDate).
GET	/api/contracts	Liste tous les contrats (utile pour debug / admin).
GET	/api/contracts/{id}	Retourne un contrat précis.
GET	/api/clients/{clientId}/contracts	Liste les contrats actifs d’un client (option de filtre sur updateDate).
GET	/api/clients/{clientId}/contracts/active/sum	Retourne la somme totale des montants des contrats actifs d’un client (endpoint optimisé SQL).
🧾 Exemple – Créer un contrat
POST /api/clients/2/contracts
{
  "startDate": "2025-01-01",
  "endDate": "2026-01-01",
  "costAmount": 150.75
}

🧾 Exemple – Contrats actifs filtrés par updateDate
GET /api/clients/2/contracts?updatedAfter=2025-10-01T00:00:00Z&updatedBefore=2025-10-15T00:00:00Z

🧾 Exemple – Somme des contrats actifs
GET /api/clients/2/contracts/active/sum

Réponse :

150.75

✅ Validation & Règles métier
Champ	Validation
email	Format valide requis (@Email)
phone	Regex : ^[+0-9()\\s-]{6,32}$
costAmount	>= 0
birthDate	Obligatoire pour PERSON
companyIdentifier	Obligatoire pour COMPANY
endDate	Optionnelle (contrat actif si null ou > currentDate)
updateDate	Mis à jour automatiquement côté serveur
🧠 Fonctionnement clé

Suppression client → tous ses contrats actifs voient leur endDate définie sur la date du jour, puis le client est supprimé.

Contrats actifs → (endDate IS NULL OR endDate > today).

Filtrage updateDate → permet d’obtenir uniquement les contrats modifiés dans une période donnée.

Endpoint SUM performant → calcul en base (SUM(cost_amount)) avec index (client_id, end_date).

🔍 Test rapide via Postman

Une collection Postman est disponible :
ApiFactory.postman_collection.json

Exemples :

# Créer un client PERSON
curl -X POST http://localhost:8080/api/clients \
  -H "Content-Type: application/json" \
  -d '{"type":"PERSON","name":"Alice","email":"alice@ex.com","phone":"+33 6 12 34 56 78","birthDate":"1995-04-12"}'

# Créer un contrat
curl -X POST http://localhost:8080/api/clients/1/contracts \
  -H "Content-Type: application/json" \
  -d '{"costAmount":120.5,"endDate":"2099-12-31"}'

# Somme des contrats actifs
curl http://localhost:8080/api/clients/1/contracts/active/sum

🔮 Axes d’amélioration possibles
Domaine	Amélioration possible
Sécurité	Authentification JWT + rôles (ADMIN / COUNSELOR).
Documentation	Intégrer SpringDoc / Swagger UI sur /swagger-ui.html.
Tests	Ajouter tests unitaires + intégration (JUnit + Testcontainers).
DevOps	Dockerfile dédié pour l’app + CI/CD GitHub Actions.
Performance	Cache sur la somme des contrats actifs (Caffeine/Redis).
Pagination	Sur les listes de clients et contrats (Spring Data Pageable).
Front	Développer un front Angular/React consommant l’API.
Monitoring	Ajouter Actuator, Prometheus et Grafana pour les métriques.

Développé par Arnault Douyere
Full-stack Developer (Java / Angular)

📍 Projet de démonstration pour un test technique (API Factory)
