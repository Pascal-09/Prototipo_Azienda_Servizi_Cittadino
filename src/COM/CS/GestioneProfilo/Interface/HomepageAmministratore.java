package COM.CS.GestioneProfilo.Interface;

import COM.CS.Commons.AvvisoErrore;
import COM.CS.Commons.InterfacciaTMP;
import COM.CS.ControlloPresenza.Control.ControlControlloPresenza;
import COM.CS.ControlloPresenza.Interface.VisualizzaRitardiEAssenze;
import COM.CS.Entity.Turno;
import COM.CS.Entity.Utente;
import COM.CS.GestioneComunicazioni.Interface.VisualizzaComunicazioniAdmin;
import COM.CS.GestioneComunicazioni.Interface.VisualizzaDettagliServizi;
import COM.CS.GestioneContratti.Interface.CambiaRuoloImpiegato;
import COM.CS.GestioneContratti.Interface.TrovaImpiegato;
import COM.CS.GestioneProfilo.Control.ControlAutenticazione;
import COM.CS.GestioneStipendio.Interface.VisualizzaStipendio;
import COM.CS.GestioneStipendio.Interface.VisualizzaStipendioImpiegatiDaAmministratore;
import COM.CS.GestioneTurnazione.Control.ControlGestioneTurnazione;
import COM.CS.GestioneTurnazione.Interface.RichiediMalattiaPerImpiegato;
import COM.CS.GestioneTurnazione.Interface.RichiestaPeriodoAstensione;
import COM.CS.GestioneTurnazione.Interface.VisualizzaTurnazioneDegliImpiegati;
import COM.CS.Utili.CommonMethods;
import COM.CS.Utili.DatiUtenteLoggato;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class HomepageAmministratore extends JFrame {
    private JPanel HomepageAmministratore;
    private JButton VisualizzaStipendioImpiegatiButton;
    private JButton VisualizzaRitardiEAssenzeButton;
    private JButton VisualizzaComunicazioniAmministratoreButton;
    private JButton VisualizzaTurnazioneImpiegatiButton;
    private JButton TrovaImpiegatoButton;
    private JButton VisualizzaDettagliServiziButton;
    private JButton richiediMalattiaPerLButton;
    private JButton CambiaRuoloImpiegatoButton;
    private JButton RichiediPeriodiDiAstensioneButton;
    private JButton ModificaDatiButton;
    private JButton ModificaPswButton;
    private JButton Uscita;
    private JPanel SezioneIcone;
    private JPanel SezioneTasti;

    public HomepageAmministratore(){
        setTitle("CitizenServices");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 400); // Imposta le dimensioni desiderate (larghezza x altezza)
        setLocationRelativeTo(null);
        setContentPane(HomepageAmministratore);
        setResizable(false);
        setVisible(true);
        Uscita.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ControlAutenticazione.uscita();
                dispose();
            }
        });
        ModificaDatiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ModificaDati MD = new ModificaDati();
                MD.setVisible(true);
                dispose();
            }
        });
        ModificaPswButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ModificaPsw MP = new ModificaPsw();
                MP.setVisible(true);
                dispose();
            }
        });
        VisualizzaComunicazioniAmministratoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VisualizzaComunicazioniAdmin VCA = new VisualizzaComunicazioniAdmin();
                VCA.setVisible(true);
                dispose();
            }
        });
        VisualizzaDettagliServiziButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VisualizzaDettagliServizi VDS = new VisualizzaDettagliServizi();
                VDS.setVisible(true);
                dispose();
            }
        });
        TrovaImpiegatoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TrovaImpiegato TI = new TrovaImpiegato();
                TI.setVisible(true);
                dispose();
            }
        });
        CambiaRuoloImpiegatoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CambiaRuoloImpiegato CRI = new CambiaRuoloImpiegato();
                CRI.setVisible(true);
                dispose();
            }
        });
        VisualizzaTurnazioneImpiegatiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ControlGestioneTurnazione CGT = new ControlGestioneTurnazione();
                ArrayList<Utente> listaImpiegatiTurniSettimanali = new ArrayList<>();
                CGT.ottieniImpiegatiCheHannoTurniNellaSettimanaCorrente(listaImpiegatiTurniSettimanali);
                if(listaImpiegatiTurniSettimanali.isEmpty()){
                    AvvisoErrore AE = new AvvisoErrore("nessun impiegato ha un turno durante la settimana");
                    AE.pack();
                    AE.setVisible(true);
                }
                else{
                    VisualizzaTurnazioneDegliImpiegati VTDI = new VisualizzaTurnazioneDegliImpiegati(listaImpiegatiTurniSettimanali, CGT);
                    VTDI.setVisible(true);
                    dispose();
                }
            }
        });
        richiediMalattiaPerLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RichiediMalattiaPerImpiegato RMPI = new RichiediMalattiaPerImpiegato();
                RMPI.setVisible(true);
                dispose();
            }
        });
        VisualizzaStipendioImpiegatiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VisualizzaStipendioImpiegatiDaAmministratore VSIDA = new VisualizzaStipendioImpiegatiDaAmministratore();
                VSIDA.setVisible(true);
                dispose();
            }
        });
        VisualizzaRitardiEAssenzeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Turno> turni = new ArrayList<>();
                ControlControlloPresenza CCP = new ControlControlloPresenza();
                CCP.ottieniRitardiEAssenzeDelGiorno(InterfacciaTMP.ottieniData(), turni);
                if(!CCP.controlloListaVuota(turni)){
                    VisualizzaRitardiEAssenze VREA = new VisualizzaRitardiEAssenze(turni);
                    VREA.setVisible(true);
                    dispose();
                }
                else{
                    AvvisoErrore AE = new AvvisoErrore("<html>Non ci sono turni registrati per la giornata corrente</html>");
                    AE.pack();
                    AE.setVisible(true);
                }
            }
        });
        RichiediPeriodiDiAstensioneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RichiestaPeriodoAstensione RPA = new RichiestaPeriodoAstensione();
                RPA.setVisible(true);
                dispose();
            }
        });
    }
}
