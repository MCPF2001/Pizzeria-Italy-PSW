# Pizzeria Italy PSW 🍕

Benvenuto/a nel progetto **Pizzeria Italy PSW**, un sito web creato per un esame accademico.
Questo progetto simula una piattaforma di prenotazione e gestione ordini per una pizzeria, ispirata a servizi come Just Eat. È stato sviluppato utilizzando tecnologie moderne e scalabili.

---

## 🛠️ Tecnologie Utilizzate

- **Backend**: [Spring Boot](https://spring.io/projects/spring-boot) - per la logica di business e la gestione delle API REST.
- **Frontend**: [Angular](https://angular.io/) (modalità standalone) - per l'interfaccia utente dinamica e interattiva.
- **Database**: [PostgreSQL](https://www.postgresql.org/) - per la gestione e archiviazione dei dati.
- **Autenticazione**: [Keycloak](https://www.keycloak.org/) - per gestire autenticazione e autorizzazione basata su token.

---

## 📂 Struttura del Progetto

### Backend
Il codice del backend è scritto in Java utilizzando Spring Boot. Le funzionalità includono:
- Gestione degli utenti (registrazione, login).
- Gestione dei prodotti (pizze, bevande, ecc.).
- Gestione degli ordini.

### Frontend
L'interfaccia utente è costruita in Angular ed è responsiva, pensata per offrire una navigazione intuitiva.

### Database
Il database è gestito con PostgreSQL. Utilizza tabelle per utenti, prodotti e ordini.

---

## 🚀 Come Eseguire il Progetto

### Prerequisiti
- **Java 17+**
- **Node.js 18+**
- **PostgreSQL**
- **Keycloak**

### Passaggi
1. Clona il repository:
   ```bash
   git clone https://github.com/tuo-username/pizzeria-italy-psw.git
Configura il database PostgreSQL:

Crea un database chiamato pizzeria_db.
Configura le credenziali nel file application.yml.
Configura Keycloak:

Importa il file di configurazione (se fornito) o crea manualmente un nuovo realm.
Configura un client per il frontend.
Avvia il backend:

bash
Copia codice
./mvnw spring-boot:run
Avvia il frontend:

bash
Copia codice
cd frontend
npm install
ng serve
Accedi al sito in http://localhost:4200.

👥 Contribuire
Chiunque è libero di utilizzare e modificare questo progetto a proprio piacimento. Sentiti libero/a di migliorare il codice, aggiungere funzionalità o adattarlo alle tue necessità.

📬 Contatti
Per qualsiasi domanda o suggerimento, puoi contattarmi su Telegram: @MCPF2001
