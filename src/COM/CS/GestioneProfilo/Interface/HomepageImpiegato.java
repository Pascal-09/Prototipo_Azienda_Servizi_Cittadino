package COM.CS.GestioneProfilo.Interface;

import COM.CS.Commons.AvvisoErrore;
import COM.CS.ControlloPresenza.Control.ControlControlloPresenza;
import COM.CS.ControlloPresenza.Interface.VisualizzaPresenze;
import COM.CS.Entity.Turno;
import COM.CS.GestioneComunicazioni.Interface.VisualizzaComunicazioniImpiegato;
import COM.CS.GestioneProfilo.Control.ControlAutenticazione;
import COM.CS.GestioneStipendio.Interface.VisualizzaStipendio;
import COM.CS.GestioneTurnazione.Control.ControlGestioneTurnazione;
import COM.CS.GestioneTurnazione.Interface.RichiestaPeriodoAstensione;
import COM.CS.GestioneTurnazione.Interface.VisualizzaTurnazione;
import COM.CS.Utili.DatiUtenteLoggato;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class HomepageImpiegato extends JFrame {
    private JPanel HomepageImpiegato;
    private JButton visualizzaTurnazioneButton;
    private JButton visualizzaStipendioButton;
    private JButton visualizzaPresenzeButton;
    private JButton richiediPeriodiDiAstensioneButton;
    private JButton modificaPasswordButton;
    private JButton modificaDatiButton;
    private JButton uscitaButton;
    private JLabel HomepageImpiegatoIcona;
    private JLabel ImpiegatoHP1Icona;
    private JLabel ImpiegatoHP2Icona;
    private JButton VisualizzaComunicazioniImpiegato;
    private JPanel Spazio1;
    private JPanel Spazio2;
    private JPanel SezioneTasti;
    private JPanel SezioneIcone;

    public HomepageImpiegato(){
        setTitle("CitizenServices");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 300); // Imposta le dimensioni desiderate (larghezza x altezza)
        setLocationRelativeTo(null);
        setContentPane(HomepageImpiegato);
        setResizable(false);
        setVisible(true);
        uscitaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ControlAutenticazione.uscita();
                dispose();
            }
        });
        VisualizzaComunicazioniImpiegato.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VisualizzaComunicazioniImpiegato VCI = new VisualizzaComunicazioniImpiegato();
                VCI.setVisible(true);
                dispose();
            }
        });
        modificaDatiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ModificaDati MD = new ModificaDati();
                MD.setVisible(true);
                dispose();
            }
        });
        modificaPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ModificaPsw MP = new ModificaPsw();
                MP.setVisible(true);
                dispose();
            }
        });
        visualizzaTurnazioneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ControlGestioneTurnazione CGT = new ControlGestioneTurnazione();
                ArrayList<Turno> turni = new ArrayList<>();
                DatiUtenteLoggato DUL = new DatiUtenteLoggato();
                CGT.ottieniTurniSettimanali(DUL.getUtente().getMatricola(), turni);
                if(!CGT.controlloListaTurniVuota(turni)){
                    VisualizzaTurnazione VT = new VisualizzaTurnazione(turni, DUL, CGT);
                    VT.setVisible(true);
                    dispose();
                }
                else {
                    AvvisoErrore AE = new AvvisoErrore("non ci sono turni questa settimana");
                    AE.pack();
                    AE.setVisible(true);
                }
            }
        });
        richiediPeriodiDiAstensioneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RichiestaPeriodoAstensione RPA = new RichiestaPeriodoAstensione();
                RPA.setVisible(true);
                dispose();
            }
        });
        visualizzaStipendioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VisualizzaStipendio VS = new VisualizzaStipendio();
                VS.setVisible(true);
                dispose();
            }
        });
        visualizzaPresenzeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DatiUtenteLoggato DUL = new DatiUtenteLoggato();
                ArrayList<Turno> turni = new ArrayList<>();
                ControlControlloPresenza CCP = new ControlControlloPresenza();
                CCP.ottieniListaPresenzeImpiegato(turni);
                if(CCP.controlloListaVuota(turni)){
                    AvvisoErrore AE = new AvvisoErrore("<html>Non è stato possibile reperire<br>le date relative ai turni<br>svolti nell’ultimo mese");
                    AE.pack();
                    AE.setVisible(true);
                }
                else{
                    VisualizzaPresenze VP = new VisualizzaPresenze(turni, CCP, DUL);
                    VP.setVisible(true);
                    dispose();
                }
            }
        });
    }

}
