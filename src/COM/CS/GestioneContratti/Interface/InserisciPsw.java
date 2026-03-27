package COM.CS.GestioneContratti.Interface;

import COM.CS.Commons.AvvisoErrore;
import COM.CS.Commons.AvvisoSuccesso;
import COM.CS.Entity.Utente;
import COM.CS.GestioneContratti.Control.ControlGestioneContratti;
import COM.CS.Utili.DatiUtenteLoggato;

import javax.swing.*;
import java.util.Arrays;

public class InserisciPsw extends JFrame{
    private JPanel InserisciPsw;
    private JButton ConfermaButton;
    private JPasswordField PasswordInput;
    private JButton Chiudi;

    public InserisciPsw(String matricolaImpiegato, String dataLicenziamento) {
        setTitle("CitizenServices");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 320); // Imposta le dimensioni desiderate (larghezza x altezza)
        setLocationRelativeTo(null);
        setContentPane(InserisciPsw);
        setResizable(false);
        ControlGestioneContratti CGC = new ControlGestioneContratti();
        DatiUtenteLoggato DUL = new DatiUtenteLoggato();
        Utente utente = DUL.getUtente();
        String psw = CGC.ottieniPsw(utente.getMatricola());
        ConfermaButton.addActionListener(e -> {
            String inputPsw = PasswordInput.getText();
            System.out.println(inputPsw);
            boolean passwordCheck = CGC.confrontaPsw(inputPsw,psw);
            if(passwordCheck){
                boolean firstModify = CGC.cambiaStatoImpiegatoUtenti(matricolaImpiegato);
                boolean secondModify = CGC.cambiaStatoImpiegatoContratti(matricolaImpiegato, dataLicenziamento);
                if(firstModify && secondModify){
                    AvvisoSuccesso AS = new AvvisoSuccesso("il contratto è stato eliminato con successo");
                    AS.pack();
                    AS.setVisible(true);
                }
                else{
                    AvvisoErrore AE = new AvvisoErrore("non è stata apportata alcuna modifica");
                    AE.pack();
                    AE.setVisible(true);
                }
                //CGC.mandaMailTerminazioneContratto(utente.getMail());
                TerminaContratto TC = new TerminaContratto();
                TC.setVisible(true);
                dispose();
            }
            else{
                AvvisoErrore AE = new AvvisoErrore("la password inserita è errata");
                AE.pack();
                AE.setVisible(true);
                TerminaContratto TC =  new TerminaContratto();
                TC.setVisible(true);
                dispose();
            }
        });
        Chiudi.addActionListener(e -> {
            TerminaContratto TC = new TerminaContratto();
            TC.setVisible(true);
            dispose();
        });
    }
}
