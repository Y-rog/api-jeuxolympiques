# 🏅 API Billetterie Jeux Olympiques — Backend Spring Boot

API REST backend de l'application de billetterie des Jeux Olympiques.
Permet la gestion des événements, la réservation de billets avec simulation de paiement et la génération d'e-billets avec QR code unique.

## 🔗 Liens

| | URL |
|---|---|
| 🌐 Front-end | https://jeuxolympiques-kappa.vercel.app |
| 📖 Swagger | https://web-production-acdd9.up.railway.app/swagger-ui/index.html |
| 🔙 GitHub Front | https://github.com/Y-rog/jeuxolympiques |

## 🧪 Compte de démonstration

| Rôle | Email | Mot de passe |
|---|---|---|
| Utilisateur | usertest@mail.com | Test123@ |

> 💡 Un compte admin est disponible sur demande pour les recruteurs.

## ✨ Fonctionnalités

- Inscription / Connexion avec JWT
- Consultation des événements et offres disponibles
- Ajout d'offres au panier via messagerie asynchrone (RabbitMQ)
- Simulation de paiement
- Génération d'e-billets avec QR code unique (ZXing)
- Espace administrateur : CRUD événements/offres, statistiques de ventes

## 🛠️ Stack technique

| Technologie | Rôle |
|---|---|
| Java 21 + Spring Boot 3.4.3 | Framework backend |
| Spring Security + JWT | Authentification |
| PostgreSQL | Base de données |
| RabbitMQ / CloudAMQP | Messagerie asynchrone |
| MapStruct | Mapping entités/DTOs |
| ZXing | Génération QR code |
| SpringDoc OpenAPI | Documentation Swagger |
| Docker | Containerisation |
| Railway | Déploiement cloud |

## 🚀 Installation locale

### Prérequis
- Java 21
- Maven 3.9+
- PostgreSQL
- RabbitMQ

### Étapes

```bash
git clone https://github.com/Y-rog/api-jeuxolympiques.git
cd api-jeuxolympiques
```

Créer un fichier `.env` à la racine :

```env
DB_URL=jdbc:postgresql://localhost:5432/jeuxolympiques
DB_USERNAME=postgres
DB_PASSWORD=votremotdepasse

RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=guest
RABBITMQ_PASSWORD=guest
RABBITMQ_VIRTUAL_HOST=/
RABBITMQ_SSL_ENABLED=false

JWT_SECRET=votresecretjwtminimum32caracteres

FRONT_URL=http://localhost:4200
SWAGGER_URL=http://localhost:8081

ADMIN_FIRSTNAME=Admin
ADMIN_LASTNAME=Admin
ADMIN_USERNAME=admin@admin.com
ADMIN_PASSWORD=votremotdepasse

ITEM_EXPIRATION_DURATION=60
```

Lancer l'application :

```bash
mvn spring-boot:run
```

Accès Swagger : `http://localhost:8081/swagger-ui/index.html`

## 🔒 Sécurité

- Authentification JWT générés à la connexion
- Mots de passe hashés avec BCrypt
- Routes protégées via Spring Security
- QR code sécurisé : clé secrète utilisateur + UUID transaction
- Validation des entrées avec Jakarta Validation

## 🧪 Tests

Tests unitaires disponibles via JUnit et Spring Boot Test :

```bash
mvn test
```

## 🔮 Évolutions futures

- Paiement réel via Stripe
- Authentification 2FA
- Envoi des e-billets par email
- Notifications à l'approche des événements
- Gestion des remboursements (SAV)
- Tableau de bord admin enrichi

## 👤 Auteur

Grégory Fulgueiras — [Portfolio](https://y-rog.com) · [LinkedIn](https://www.linkedin.com/in/fulgueiras-grégory-887267136/)