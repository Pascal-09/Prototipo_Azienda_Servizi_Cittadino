package COM.CS.GestioneContratti.Interface;

import COM.CS.Entity.Contratto;
import COM.CS.Entity.Utente;
import COM.CS.GestioneContratti.Control.ControlGestioneImpiegati;
import COM.CS.Utili.CommonMethods;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VisualizzaDettagliImpiegato extends JFrame {
    private JPanel VisualizzaDettagliImpiegato;
    private JButton IndietroButton;
    private JLabel NomeText;
    private JLabel CognomeText;
    private JLabel matricolaText;
    private JLabel ruoloText;
    private JLabel telefonoText;
    private JLabel eMailText;
    private JLabel dataDiAssunzioneText;
    private JLabel tipoDiContrattoText;
    private JLabel NomeLabel;
    private JLabel CognomeLabel;
    private JLabel MatricolaLabel;
    private JLabel RuoloLabel;
    private JLabel TelefonoLabel;
    private JLabel MailLabel;
    private JLabel AssunzioneLabel;
    private JLabel ContrattoLabel;
    private JPanel SezioneTesto;

    public VisualizzaDettagliImpiegato(Utente utente){
        setTitle("CitizenServices");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600); // Imposta le dimensioni desiderate (larghezza x altezza)
        setLocationRelativeTo(null);
        setContentPane(VisualizzaDettagliImpiegato);
        setResizable(false);

        ControlGestioneImpiegati CGI = new ControlGestioneImpiegati();
        Contratto contratto = CGI.ottieniContrattoUtente(utente);

        NomeText.setText(utente.getNome());
        CognomeText.setText(utente.getCognome());
        matricolaText.setText(utente.getMatricola());
        ruoloText.setText(CommonMethods.ottieniStringaRuolo(utente.getRuolo()));
        telefonoText.setText(utente.getTelefono());
        eMailText.setText(utente.getMail());
        dataDiAssunzioneText.setText(contratto.getData_assunzione());
        tipoDiContrattoText.setText(contratto.getTipo_contratto());


        IndietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TrovaImpiegato TI = new TrovaImpiegato();
                TI.setVisible(true);
                dispose();
            }
        });
    }
}
