package COM.CS.GestioneTurnazione.Interface;

import COM.CS.Commons.AvvisoErrore;
import COM.CS.Entity.Turno;
import COM.CS.Entity.Utente;
import COM.CS.GestioneTurnazione.Control.ControlGestioneTurnazione;
import COM.CS.Utili.CommonMethods;
import COM.CS.Utili.DatiUtenteLoggato;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VisualizzaTurnazione extends JFrame{
    private JPanel VisualizzaTurnazione;
    private JButton ChiudiButton;
    private JList ListaTurni;
    private JLabel TitoloEsplicativo;

    public VisualizzaTurnazione(ArrayList<Turno> turni, DatiUtenteLoggato DUL, ControlGestioneTurnazione CGT) {
        setTitle("CitizenServices");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 300); // Imposta le dimensioni desiderate (larghezza x altezza)
        setLocationRelativeTo(null);
        setContentPane(VisualizzaTurnazione);
        setResizable(false);
        Utente utente = DUL.getUtente();
        ChiudiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommonMethods.mostraHomepage(utente.getRuolo());
                dispose();
            }
        });
        TitoloEsplicativo.setText("Turnazione Settimanale(" + DUL.getTipoCotratto() + ")");
        DefaultListModel<String> listModel = new DefaultListModel<>();
        ListaTurni.setModel(listModel);
        for (Turno turno : turni) {
            listModel.addElement(turno.toString());
        }

        setVisible(true);
    }
}
