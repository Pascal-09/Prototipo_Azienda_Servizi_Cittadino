package COM.CS.Utili;

import COM.CS.Commons.InterfacciaTMP;
import COM.CS.Entity.Richiesta;
import COM.CS.Entity.Turno;
import COM.CS.Entity.Utente;
import COM.CS.GestioneComunicazioni.Control.ControlNotificaRitardoAssenza;
import COM.CS.GestioneProfilo.Interface.Autenticazione;
import COM.CS.GestioneStipendio.Control.ControlGestioneStipendio;
import COM.CS.GestioneTurnazione.Control.ControlGestioneTurnazione;

import java.util.ArrayList;
import java.util.Timer;

public class Main {
    public static void main(String[] args){
        Timer timer = new Timer(); //creazione timer, ovvero conteggia il tempo
        //creazione control gestione turnazione, gestione stipendio, notifica ritardo e assenza
        ControlGestioneTurnazione CGT = new ControlGestioneTurnazione();
        ControlGestioneStipendio CGS = new ControlGestioneStipendio();
        ControlNotificaRitardoAssenza.notificaRitardi(timer); //ogni 20 minuti controlla se è presente un ritardo e lo notifica
        ControlNotificaRitardoAssenza.notificaAssenze(timer); //ogni 20 minuti controlla se è presente un'assenza e la notifica
        if(CGT.controlloGiornoCalcoloTurnazione(InterfacciaTMP.ottieniData())){ //se è il giorno in cui deve venire calcolata la turnazione
            ArrayList<Utente> impiegati = new ArrayList<>();
            CGT.ottieniImpiegatiConStatoAttivo(impiegati); //ottiene gli impiegati non licenziati
            ArrayList<Richiesta> richieste = new ArrayList<>();
            CGT.ottieniListaPeriodiAstensioneApprovati(richieste); //ottiene le richieste di periodo d'astensione
            ArrayList<Turno> turni = new ArrayList<>();
            CGT.calcolaTurnazione(impiegati, richieste, turni); //dai dati precedentemente ricavati, calcola la nuova turnazione
            for(Turno turno: turni){
                CGT.registraNuovaTurnazione(turno);
            }
        }
        if(CGS.controlloGiornoCalcoloStipendio(InterfacciaTMP.ottieniData())){ //se è il giorno del calcolo dello stipendio mensile
            CGS.calcolaStipendioMensile(); //calcola lo stipendio mensile
        }

        DatiUtenteLoggato DUL = new DatiUtenteLoggato(); //preparazione al salvataggio dati dell'utente
        Utente utente  = DUL.getUtente();
        if(utente.getMatricola().isBlank()){ //se non c'è alcun utente già registrato o i suoi dati sono corrotti
            DUL.eliminaDatiUtente(); //elimina preventivamente tutti i dati salvati sul sistema dell'utente
            Autenticazione A = new Autenticazione(); //manda l'utente alla schermata di autenticazione
            A.setVisible(true);
        }
        else{//se c'è un utente già autenticato
            CommonMethods.mostraHomepage(DUL.getUtente().getRuolo());
        }
    }
}
