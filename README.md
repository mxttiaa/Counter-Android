# Counter App (v2.1)

Applicazione Android moderna e minimalista per il conteggio personalizzato, sviluppata in Java con supporto alla persistenza locale dei dati e Material Design.

## 🌟 Novità della Versione 2.1

* **Colore Fisso di Sfondo:** Selettore visivo per impostare un colore statico preferito quando il cambio dinamico è disattivato.
* **Navigazione Integrata:** Supporto nativo ai gesti di navigazione Android (`OnBackPressedDispatcher`) per salvare automaticamente le preferenze anche uscendo con swipe o tasto indietro.
* **Reset Intelligente:** Il reset azzera il contatore ma preserva il colore fisso scelto dall'utente se il cambio automatico è disattivato.
* **UI & UX Refinement:** Dialog di reset riprogettato con sfondo `honeydew` ad alto contrasto per una migliore leggibilità.
* **APK Firmato Ufficiale:** Rilasciata la prima versione firmata con chiave di produzione (Release).

## ⚙️ Funzionalità Principali

* **Conteggio Flessibile:** Incremento e decremento con passo (Gap) configurabile da 1 a 100.
* **Personalizzazione Sfondo:**
    * **Modalità Dinamica:** Cambio colore automatico a intervalli personalizzabili (senza ripetizioni consecutive).
    * **Modalità Statica:** Selezione manuale tra 16 tonalità pastello.
* **Persistenza Dati:** Salvataggio automatico di contatore e configurazioni tramite `SharedPreferences`.
* **Controlli di Sicurezza:** Limiti fisati tra `0` e `99999` con avvisi Toast e Dialog di conferma per il reset.

## 🎨 Palette Colori

Integrazione di uno stile grafico ad alto contrasto basato su componenti Material 3 e colori dedicati (*Steel Blue*, *Deep Space Blue*, *Strawberry Red* e sfondi pastello *Light*).

## 📋 Changelog

* **v2.1:** Introdotto il selettore di colore fisso, supporto alle gesture di navigazione, reset condizionato e restyling visivo del dialog.
* **v2.0:** Aggiunta la schermata *Settings*, gestione del Gap, persistenza con `SharedPreferences` e controllo dell'intervallo colori.
* **v1.0.1:** Ottimizzazione algoritmo di selezione colori casuali e ampliamento palette.

## 📥 Download

Scarica l'APK più recente dalla sezione [Releases](https://github.com/mxttiaa/Counter-Android/releases).