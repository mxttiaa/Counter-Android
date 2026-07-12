# Counter App (Versione 2.0)

Un'applicazione Android dal design moderno e curato, sviluppata per esercitarsi con la programmazione ad oggetti in Java, la gestione avanzata dei layout XML e la persistenza dei dati.

## 🚀 Novità della Versione 2.0

* **Impostazioni Personalizzate:** Introdotta una nuova schermata di configurazione (Settings Activity) per un controllo totale dell'esperienza utente.
* **Valore di Salto (Gap):** Possibilità di modificare il valore di incremento e decremento tramite un mini-contatore dedicato nelle impostazioni.
* **Persistenza dei Dati (SharedPreferences):** Il contatore, il valore del salto e le impostazioni del colore vengono salvati localmente. Anche chiudendo l'applicazione o riavviando il dispositivo, non perderai i tuoi progressi.
* **Controllo Cambio Colore:** Aggiunto uno Switch nelle impostazioni per attivare o disattivare il cambio dinamico dello sfondo.
* **Frequenza Colori Modificabile:** Possibilità di scegliere ogni quanti clic deve cambiare lo sfondo (es. ogni 5 clic, 10 clic, ecc.) tramite un contatore dedicato.
* **Interfaccia e Dialog Custom:** Grafica rifinita con una palette coerente (Steel Blue, Deep Space Blue, Strawberry Red) e un Dialog di reset completamente personalizzato via codice Java.

## ⚙️ Funzionalità Principali

* **Incremento e Decremento:** Pulsanti dedicati per aumentare o diminuire il valore in base al salto impostato.
* **Limiti di Sicurezza:** L'app impedisce di scendere sotto lo `0` e di superare il limite di `99999`, avvisando l'utente con un messaggio pop-up (Toast).
* **Reset Sicuro:** Un pulsante per azzerare istantaneamente il contatore previa conferma tramite un pop-up grafico coordinato.
* **Easter Egg Visivo:** Se attivo, lo sfondo cambia dinamicamente pescando da una palette di colori pastello senza mai ripetere lo stesso colore due volte di fila.

## 🎨 Design ed Estetica
L'interfaccia adotta uno stile minimalista ad alto contrasto basato sulle linee guida del Material Design, con componenti arrotondati e bordi solidi per garantire la massima leggibilità.

## 📦 Ultimi Aggiornamenti
- **v2.0:** Aggiunte impostazioni avanzate, salvataggio dati locale (SharedPreferences), switch abilitazione colori, intervallo dinamico e restyling completo del Dialog.
- **v1.0.1:** Ottimizzazione logica colori (evitata la ripetizione consecutiva) e aggiunta di 10 nuovi colori pastello.

## 📥 Download
Puoi scaricare il file installabile APK aggiornato direttamente dalla sezione [Releases](https://github.com/mxttiaa/Counter-Android/releases).