package COM.CS.GestioneProfilo.Interface;

import COM.CS.Commons.AvvisoErrore;
import COM.CS.Utili.CommonMethods;
import COM.CS.Utili.DatiUtenteLoggato;
import COM.CS.GestioneProfilo.Control.ControlModificaDati;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ModificaPsw extends JFrame {
    private JPanel ModificaPsw;
    private JPasswordField NuovaPswInput;
    private JPasswordField ConfermaPswInput;
    private JButton ConfermaModificaButton;
    private JButton ChiudiButton;
    private JLabel ConfermaPswText;
    private JLabel NuovaPswText;
    private JLabel NuovaPswIcona;
    private JLabel ConfermaPswIcona;
    private JPanel SezioneIcone;
    private JPanel SezioneInterazione;

    public ModificaPsw(){
        setTitle("CitizenServices");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400); // Imposta le dimensioni desiderate (larghezza x altezza)
        setLocationRelativeTo(null);
        setContentPane(ModificaPsw);
        setResizable(false);
        setVisible(true);
        ConfermaModificaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String NPI = new String(NuovaPswInput.getPassword());
                String CPI = new String(ConfermaPswInput.getPassword());
                ControlModificaDati CMD = new ControlModificaDati();
                DatiUtenteLoggato DUL = new DatiUtenteLoggato();
                boolean controlloPsw = CMD.controlloPsw(NPI,CPI);
                if(controlloPsw){
                        CMD.modificaPsw(DUL.getUtente().getMatricola(),NPI);
                }
                else{
                    AvvisoErrore AE = new AvvisoErrore("la password inserita e la conferma della password non coincidono, riprova");
                    AE.pack();
                    AE.setVisible(true);
                }
            }
        });
        ChiudiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DatiUtenteLoggato DUL = new DatiUtenteLoggato();
                int ruolo = DUL.getUtente().getRuolo();
                CommonMethods.mostraHomepage(ruolo);
                dispose();
            }
        });
    }
}
