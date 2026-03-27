package COM.CS.GestioneContratti.Interface;

import COM.CS.Commons.AvvisoErrore;
import COM.CS.Commons.AvvisoSuccesso;
import COM.CS.Commons.InterfacciaTMP;
import COM.CS.Entity.Contratto;
import COM.CS.Entity.Utente;
import COM.CS.GestioneContratti.Control.ControlGestioneContratti;
import COM.CS.Utili.CommonMethods;
import COM.CS.Utili.DatiUtenteLoggato;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class NuovoContratto extends JFrame{
    private JPanel NuovoContratto;
    private JTextField NomeInput;
    private JTextField CognomeInput;
    private JTextField EmailInput;
    private JTextField ResidenzaInput;
    private JButton registraNuovoContrattoButton;
    private JTextField IBANInput;
    private JTextField CDFiscaleInput;
    private JTextField TelefonoInput;
    private JComboBox<String> TipoImpiegatoInput;
    private JLabel RuoloLabel;
    private JComboBox RuoloInput;
    private JButton Chiudi;


    public NuovoContratto(){
        setTitle("CitizenServices");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 600); // Imposta le dimensioni desiderate (larghezza x altezza)
        setLocationRelativeTo(null);
        setContentPane(NuovoContratto);
        setResizable(false);

        String[] opzioniImpiegato = {"Full-Time", "Part-Time"};
        for(String opzione : opzioniImpiegato){
            TipoImpiegatoInput.addItem(opzione);
        }

        int[] ruoloImpiegato = {1,2,3,4};
        for(int ruolo : ruoloImpiegato){
            RuoloInput.addItem(ruolo);
        }

        DatiUtenteLoggato DUL = new DatiUtenteLoggato();
        int utenteRuolo = DUL.getUtente().getRuolo();
        ControlGestioneContratti CGC = new ControlGestioneContratti();

        setVisible(true);

        registraNuovoContrattoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomeInput = NomeInput.getText();
                String cognomeInput = CognomeInput.getText();
                String emailInput = EmailInput.getText();
                String residenzaInput = ResidenzaInput.getText();
                String ibanInput = IBANInput.getText();
                String codFiscale = CDFiscaleInput.getText();
                String telefonoInput = TelefonoInput.getText();

                int index1 = TipoImpiegatoInput.getSelectedIndex();
                String tipo_contratto = opzioniImpiegato[index1];

                int index2 = RuoloInput.getSelectedIndex();
                int ruolo = ruoloImpiegato[index2];

                boolean controlloSintassiContratto = CGC.controlloSintrassiContratto(nomeInput,cognomeInput,emailInput,residenzaInput,ibanInput,codFiscale,telefonoInput,tipo_contratto);
                boolean verificaEsistenzaCodiceFiscale = CGC.verificaEsistenzaCodiceFiscale(codFiscale);
                if(controlloSintassiContratto && verificaEsistenzaCodiceFiscale){
                    String matricola = CGC.creaMatricola(codFiscale);
                    int cod_contratto = CGC.calcolaCodiceContratto();
                    String tempo = InterfacciaTMP.ottieniData();
                    int ore_da_svolgere = 0;
                    if(tipo_contratto.equals("Part-Time")){
                        ore_da_svolgere = 18;
                    }
                    if (tipo_contratto.equals("Full-Time")) {
                        ore_da_svolgere = 36;
                    }
                    Utente utente = new Utente(matricola , cod_contratto, nomeInput, cognomeInput, telefonoInput, ibanInput, emailInput, ruolo, 1, codFiscale, ore_da_svolgere);
                    Contratto contratto = new Contratto(cod_contratto,null,tempo, 1, tipo_contratto);
                    String psw = CGC.creaPsw();
                    CGC.aggiungiContratto(contratto);
                    CGC.aggiungiUtente(utente, cod_contratto, psw);
                    AvvisoSuccesso AS = new AvvisoSuccesso("il contratto è stato registrato con successo");
                    AS.pack();
                    AS.setVisible(true);
                    CommonMethods.mostraHomepage(utenteRuolo);
                    dispose();
                }
                else{
                    AvvisoErrore AE = new AvvisoErrore("i dati inseriti non sono validi o l'impiegato è già registrato");
                    AE.pack();
                    AE.setVisible(true);
                    CommonMethods.mostraHomepage(utenteRuolo);
                    dispose();
                }

            }
        });
        Chiudi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DatiUtenteLoggato DUL = new DatiUtenteLoggato();
                CommonMethods.mostraHomepage(DUL.getUtente().getRuolo());
                dispose();
            }
        });
    }
}
