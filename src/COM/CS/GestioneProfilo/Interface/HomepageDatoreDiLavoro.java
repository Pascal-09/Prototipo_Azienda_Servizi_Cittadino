package COM.CS.GestioneProfilo.Interface;

import COM.CS.Commons.AvvisoErrore;
import COM.CS.Entity.Richiesta;
import COM.CS.Entity.Stipendio;
import COM.CS.Entity.Utente;
import COM.CS.GestioneComunicazioni.Interface.InviaComunicazione;
import COM.CS.GestioneContratti.Interface.NuovoContratto;
import COM.CS.GestioneContratti.Interface.TerminaContratto;
import COM.CS.GestioneProfilo.Control.ControlAutenticazione;
import COM.CS.GestioneStipendio.Control.ControlGestioneStipendio;
import COM.CS.GestioneStipendio.Interface.AccreditaStipendio;
import COM.CS.GestioneTurnazione.Control.ControlGestioneRichieste;
import COM.CS.GestioneTurnazione.Interface.ApprovazioneRichiesta;
import COM.CS.Utili.CommonMethods;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class HomepageDatoreDiLavoro extends JFrame {
    private JPanel HomepageDatoreDiLavoro;
    private JButton NuovoContrattoButton;
    private JButton TerminazioneContrattoButton;
    private JButton ApprovazioneRichiestaButton;
    private JButton AccreditaStipendioButton;
    private JButton ModificaDatiButton;
    private JButton ModificaPasswordButton;
    private JButton UscitaButton;
    private JLabel IconaDatoreDiLavoro;
    private JPanel SezioneIcone;
    private JPanel SezioneTasti;
    private JButton InviaComunicazioneButton;

    public HomepageDatoreDiLavoro(){
        setTitle("CitizenServices");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 400); // Imposta le dimensioni desiderate (larghezza x altezza)
        setLocationRelativeTo(null);
        setContentPane(HomepageDatoreDiLavoro);
        setResizable(false);
        setVisible(true);
        UscitaButton.addActionListener(new ActionListener() {
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
        ModificaPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ModificaPsw MP = new ModificaPsw();
                MP.setVisible(true);
                dispose();
            }
        });
        InviaComunicazioneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InviaComunicazione IC = new InviaComunicazione();
                IC.setVisible(true);
                dispose();
            }
        });
        NuovoContrattoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NuovoContratto NC = new NuovoContratto();
                NC.setVisible(true);
                dispose();
            }
        });
        TerminazioneContrattoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TerminaContratto TC = new TerminaContratto();
                TC.setVisible(true);
                dispose();
            }
        });
        ApprovazioneRichiestaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ControlGestioneRichieste CGR = new ControlGestioneRichieste();
                ArrayList<Richiesta> listaRichieste = new ArrayList<>();
                CGR.ottieniListaRichiesteInSospeso(listaRichieste);
                if(CGR.controlloListaRichiesteVuota(listaRichieste)){
                    AvvisoErrore AE = new AvvisoErrore("non ci sono richieste di astensione da approvare o rifiutare");
                    AE.pack();
                    AE.setVisible(true);
                }
                else {
                    ApprovazioneRichiesta AR = new ApprovazioneRichiesta(listaRichieste,CGR);
                    AR.setVisible(true);
                    dispose();
                }
            }
        });
        AccreditaStipendioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ArrayList<Stipendio> stipendi = new ArrayList<>();
                ArrayList<Utente> impiegati = new ArrayList<>();
                ControlGestioneStipendio CGS = new ControlGestioneStipendio();
                CGS.ottieniListaImpiegatiDaPagare(stipendi, impiegati);
                if(CGS.controlloListaVuota(stipendi, impiegati)){//lista vuota
                    AvvisoErrore AE = new AvvisoErrore("Non ci sono stipendi da accreditare");
                    AE.pack();
                    AE.setVisible(true);
                }
                else{
                    AccreditaStipendio AS = new AccreditaStipendio(stipendi, impiegati, CGS);
                    AS.setVisible(true);
                    dispose();
                }
            }
        });
    }

}
