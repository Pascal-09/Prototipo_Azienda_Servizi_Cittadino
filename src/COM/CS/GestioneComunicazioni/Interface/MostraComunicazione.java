package COM.CS.GestioneComunicazioni.Interface;

import COM.CS.Entity.Comunicazione;
import COM.CS.Utili.DatiUtenteLoggato;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MostraComunicazione  extends JFrame {
    private JPanel MostraComunicazione;
    private JTextArea messaggioText;
    private JButton ChiudiButton;
    private JLabel DettagliComunicazioneLabel;
    private JLabel OggettoLabel;
    private JLabel MittenteLabel;
    private JLabel ContenutoComunicazione;
    private JLabel DataOraLabel;

    public MostraComunicazione(Comunicazione comunicazione){
        setTitle("CitizenServices");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 300); // Imposta le dimensioni desiderate (larghezza x altezza)
        setLocationRelativeTo(null);
        setContentPane(MostraComunicazione);
        setResizable(false);

        messaggioText.setText(comunicazione.getTesto());
        OggettoLabel.setText(comunicazione.getOggetto());
        MittenteLabel.setText(comunicazione.getRef_mittente());
        DataOraLabel.setText(comunicazione.getOrarioInvio());
        setVisible(true);


        ChiudiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DatiUtenteLoggato DUL = new DatiUtenteLoggato();
                if(DUL.getUtente().getRuolo() == -1){
                    VisualizzaComunicazioniAdmin VCA = new VisualizzaComunicazioniAdmin();
                    VCA.setVisible(true);
                    dispose();
                }
                else{
                    VisualizzaComunicazioniImpiegato VCI = new VisualizzaComunicazioniImpiegato();
                    VCI.setVisible(true);
                    dispose();
                }
            }
        });
    }
}
