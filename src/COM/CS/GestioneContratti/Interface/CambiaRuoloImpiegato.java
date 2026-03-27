package COM.CS.GestioneContratti.Interface;

import COM.CS.Commons.AvvisoErrore;
import COM.CS.Commons.AvvisoSuccesso;
import COM.CS.Entity.Utente;
import COM.CS.GestioneContratti.Control.ControlGestioneContratti;
import COM.CS.Utili.CommonMethods;
import COM.CS.Utili.DatiUtenteLoggato;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CambiaRuoloImpiegato extends JFrame{
    private JPanel CambiaRuoloImpiegato;
    private JTextField MatricolaInput;
    private JComboBox RuoloInput;
    private JButton ConfermaButton;
    private JButton ChiudiButton;
    private JLabel TestoMatricola;
    private JLabel NuovoRuoloTesto;
    private JPanel SezioneTasti;

    public CambiaRuoloImpiegato(){
        setTitle("CitizenServices");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 200); // Imposta le dimensioni desiderate (larghezza x altezza)
        setLocationRelativeTo(null);
        setContentPane(CambiaRuoloImpiegato);
        setResizable(false);
        setVisible(true);

        int ruoloOpzioni[] = {1,2,3,4};
        for(int i : ruoloOpzioni){
            RuoloInput.addItem(i);
        }

        ControlGestioneContratti CGC = new ControlGestioneContratti();

        DatiUtenteLoggato DUL = new DatiUtenteLoggato();
        Utente utente = DUL.getUtente();
        ChiudiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommonMethods.mostraHomepage(utente.getRuolo());
                dispose();
            }
        });
        ConfermaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String matricolaImpiegato = MatricolaInput.getText();
                int index = RuoloInput.getSelectedIndex();
                int nuovoRuolo = ruoloOpzioni[index];
                boolean checkMatricola = CGC.controlloEsistenzaMatricola(matricolaImpiegato);
                if(checkMatricola){
                    boolean ruoloAggiornato = CGC.aggiornaRuolo(matricolaImpiegato, nuovoRuolo);
                    if(ruoloAggiornato){
                        AvvisoSuccesso AS = new AvvisoSuccesso("il cambio di ruolo è avvenuto con successo");
                        AS.pack();
                        AS.setVisible(true);
                        CommonMethods.mostraHomepage(utente.getRuolo());
                        dispose();
                    }
                    else{
                        AvvisoErrore AE = new AvvisoErrore("nessuna modifica apportata: il ruolo è uguale al precedente");
                        AE.pack();
                        AE.setVisible(true);
                    }
                }
                else{
                    AvvisoErrore AE = new AvvisoErrore("i dati inseriti non sono validi. Reinserirl");
                    AE.pack();
                    AE.setVisible(true);
                }
            }
        });
    }
}
