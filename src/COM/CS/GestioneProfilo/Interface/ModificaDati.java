package COM.CS.GestioneProfilo.Interface;

import COM.CS.Commons.AvvisoErrore;
import COM.CS.Utili.CommonMethods;
import COM.CS.Utili.DatiUtenteLoggato;
import COM.CS.Entity.Utente;
import COM.CS.GestioneProfilo.Control.ControlModificaDati;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ModificaDati extends JFrame{
    private JPanel ModificaDati;
    private JTextField TelefonoInput;
    private JTextField EmailInput;
    private JTextField IBANInput;
    private JButton ConfermaButton;
    private JButton AnnullaButton;
    private JLabel IconaDecorativaModificaDati;
    private JPanel SezioneDecorazione;
    private JPanel SezioneIcone;
    private JPanel SezioneInterazione;
    private JLabel TelefonoText;
    private JLabel EmailText;
    private JLabel IBANText;
    private JLabel IconaTelefono;
    private JLabel IconaEmail;
    private JLabel IconaIBAN;
    private JLabel IconaClicca;

    public ModificaDati(){
        setTitle("CitizenServices");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400); // Imposta le dimensioni desiderate (larghezza x altezza)
        setLocationRelativeTo(null);
        setContentPane(ModificaDati);
        setResizable(false);
        setVisible(true);
        DatiUtenteLoggato DUL = new DatiUtenteLoggato();
        Utente utente = DUL.getUtente();
        IBANInput.setText(utente.getIBAN());
        EmailInput.setText(utente.getMail());
        TelefonoInput.setText(utente.getTelefono());
        AnnullaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommonMethods.mostraHomepage(utente.getRuolo());
                dispose();
            }
        });
        ConfermaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ControlModificaDati CMD = new ControlModificaDati();
                String email = new String(EmailInput.getText());
                String IBAN = new String(IBANInput.getText());
                String telefono = new String(TelefonoInput.getText());
                boolean controlloModifiche = CMD.controlloModifiche(email, IBAN, telefono);
                if(controlloModifiche){
                    DatiUtenteLoggato DUL = new DatiUtenteLoggato();
                    CMD.modificaDatiUtente(DUL.getUtente().getMatricola(), email, IBAN, telefono);
                    DUL.modificaDatiUtente(email, IBAN, telefono);
                    dispose();
                    ModificaDati MD = new ModificaDati();

                }
                else{
                    AvvisoErrore AE = new AvvisoErrore("i dati immessi non sono corretti sintatticamente");
                    AE.pack();
                    AE.setVisible(true);
                }
            }
        });
    }
}

