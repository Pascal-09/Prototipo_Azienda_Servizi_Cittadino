package COM.CS.GestioneTurnazione.Interface;

import COM.CS.Commons.AvvisoErrore;
import COM.CS.Commons.AvvisoSuccesso;
import COM.CS.Entity.Richiesta;
import COM.CS.Entity.Turno;
import COM.CS.GestioneTurnazione.Control.ControlGestioneAssenza;
import COM.CS.GestioneTurnazione.Control.ControlGestioneRichieste;
import COM.CS.Utili.CommonMethods;
import COM.CS.Utili.DatiUtenteLoggato;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

public class RichiediMalattiaPerImpiegato extends JFrame {
    private JPanel RichiediMalattiaPerImpiegato;
    private JTextField MatricolaInput;
    private JTextField InizioMalattia;
    private JButton inviaRichiestaButton;
    private JButton chiudiButton;
    private JTextField FineMalattiaInput;
    private JTextField NomeInput;
    private JTextField CognomeInput;

    public RichiediMalattiaPerImpiegato(){
        setTitle("CitizenServices");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 500); // Imposta le dimensioni desiderate (larghezza x altezza)
        setLocationRelativeTo(null);
        setContentPane(RichiediMalattiaPerImpiegato);
        setResizable(false);
        DatiUtenteLoggato DUL = new DatiUtenteLoggato();
        ControlGestioneRichieste CGR = new ControlGestioneRichieste();
        setVisible(true);
        chiudiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommonMethods.mostraHomepage(DUL.getUtente().getRuolo());
                dispose();
            }
        });
        inviaRichiestaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String matr = MatricolaInput.getText();
                String nome = NomeInput.getText();
                String cognome = CognomeInput.getText();
                String inizio = InizioMalattia.getText();
                String fine = FineMalattiaInput.getText();
                boolean dateValide = CGR.controlloDateValide(inizio, fine);
                boolean esistenzaImp = CGR.controlloEsistenzaImpiegato(matr, nome, cognome);
                if(esistenzaImp && dateValide){
                    Richiesta richiesta = new Richiesta(Richiesta.calcolaCodiceRichiesta(), matr,4, nome, cognome, 1, inizio, fine);
                    CGR.mandaRichiestaPeriodoAstensione(richiesta);
                    ArrayList<Turno> turni = new ArrayList<>();
                    ControlGestioneAssenza CGA = new ControlGestioneAssenza(matr, inizio, fine, turni);
                    AvvisoSuccesso AS = new AvvisoSuccesso("la richiesta di malattia è stata registrata con successo");
                    AS.pack();
                    AS.setVisible(true);
                }
                else {
                    AvvisoErrore AE = new AvvisoErrore("Le informazioni inserite non sono corrette, riprova.");
                    AE.pack();
                    AE.setVisible(true);
                }
            }
        });
    }


}
