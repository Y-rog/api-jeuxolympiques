# API Jeux Olympiques – Application de billetterie en ligne

Cette API constitue le backend de l'application de billetterie des Jeux Olympiques 2024. Elle permet à des utilisateurs de réserver des billets en ligne, de les payer (simulation), et de récupérer leurs e-billets contenant un QR code unique. Un espace administrateur permet de gérer les événements et les offres.

##  Liens importants

-  Front-end (Angular 19) : [https://jeuxolympiques.gregoryfulgueiras.com](https://jeuxolympiques.gregoryfulgueiras.com)
-  Swagger API Docs : [https://api-jeuxolympiques.gregoryfulgueiras.com/swagger-ui/index.html](https://api-jeuxolympiques.gregoryfulgueiras.com/swagger-ui/index.html)
-  GitHub API : [https://github.com/Y-rog/api-jeuxolympiques](https://github.com/Y-rog/api-jeuxolympiques)
-  GitHub Front : [https://github.com/Y-rog/jeuxolympiques](https://github.com/Y-rog/jeuxolympiques)

---

##  Fonctionnalités principales

-  Inscription / Connexion d’utilisateurs
-  Ajout d’offres au panier et simulation de paiement
-  Génération d’e-billets avec QR code unique (UUID utilisateur + UUID transaction)
-  Authentification via Spring Security + JWT (stocké en sessionStorage)
-  Espace administrateur :CRUD sur événements et offres, gestion de la plublication des offres, visualisation des ventes par offre


>  Le compte administrateur ne peut pas être créé depuis l’application. Ils sont générer par l'applicaiton au démarrage.
>  Les identifiants sont stockés dans le fichier `.env`.

---

##  Stack technique

- **Backend :** Java 21, Spring Boot 3.4.3
- **Base de données :** PostgreSQL 
- **Broker :** RabbitMQ 
- **Documentation :** SpringDoc OpenAPI (Swagger)
- **QR Code :** ZXing
- **Mapper :** MapStruct
- **Authentification :** Spring Security + JWT 

---

##  Déploiement

L’API est déployée sur **Heroku**, avec :

- Addon **Heroku Postgres**
- Addon **CloudAMQP** (RabbitMQ)

---

##  Tests

Quelques tests unitaires sont disponibles via JUnit et Spring Boot Test.

---

##  Lancer le projet localement

###  Prérequis

- Java 21
- Maven 3.9+
- PostgreSQL (ou configuration via `.env`)
- RabbitMQ (local ou CloudAMQP)

###  Configuration `.env`

Créer un fichier `.env` à la racine avec les variables suivantes :

```env
DB_URL=jdbc:postgresql://<host>:<port>/<dbname>
DB_USERNAME=<your_db_username>
DB_PASSWORD=<your_db_password>

RABBITMQ_HOST=localhost (par défaut)
RABBITMQ_PORT=5672 (par défaut)
RABBITMQ_USERNAME=guest (par défaut)
RABBITMQ_PASSWORD=guest (par défaut)
RABBITMQ_VIRTUAL_HOST=/ (par défaut)
RABBITMQ_SSL_ENABLED=false (par défaut)

JWT_SECRET=<your_jwt_secret>>

FRONT_URL=http://localhost:4200 (par défaut)

ADMIN_FIRSTNAME=<your_admin_firstname>
ADMIN_LASTNAME=<your_admin_lastname>
ADMIN_USERNAME=<your_admin_username> (email)
ADMIN_PASSWORD=<your_admin_password>

ITEM_EXPIRATION_DURATION=3600 (temps de conservation d'un item dans le panier)
```

### Commandes Maven

```bash
# Pour lancer le projet
mvn spring-boot:run
```

---

##  Authentification & Sécurité

- L’authentification repose sur **JWT** générés à la connexion et stockés côté front dans le `sessionStorage`.
- Les routes sensibles sont protégées via Spring Security.
- Chaque e-billet contient un **QR code sécurisé**, généré à partir de la concaténation :
secret_ket utilisateur + UUID transaction
- BCrypt : Pour hacher et donc protéger les mots de passe en BDD
- Validation des formulaires


---

##  Fonctionnalités à venir

-  Intégration de paiements réels (ex: Stripe)
-  Optimiser les performances de l'application
-  Gestion des SAV
-  Tableau de bord plus détaillé pour l’admin
-  Authentification 2FA
-  Envoi des e-billets par mail
-  Gestion des données utilisateurs 

---

##  Auteur

Projet développé par **Grégory Fulgueiras** dans le cadre de la formation **Développeur d’applications** chez **Studi**.

---

##  Licence

Ce projet est à but éducatif et non destiné à une utilisation commerciale.
