package COM.CS.GestioneContratti.Interface;

import COM.CS.Entity.Utente;
import COM.CS.GestioneContratti.Control.ControlGestioneImpiegati;
import COM.CS.GestioneContratti.Interface.Renderer.TrovaImpiegatoCellRenderer;
import COM.CS.Utili.CommonMethods;
import COM.CS.Utili.CustomListModel;
import COM.CS.Utili.DatiUtenteLoggato;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class TrovaImpiegato extends JFrame{
    private ArrayList<Utente> listaImpiegati = new ArrayList<>();
    private ArrayList<Utente> listaFiltrata = new ArrayList<>();
    private JPanel TrovaImpiegato;
    private JList JListaImpiegati;
    private JTextField NomeInput;
    private JTextField CognomeInput;
    private JTextField MatricolaInput;
    private JButton FiltraButton;
    private JLabel NomeLabel;
    private JLabel CognomeLabel;
    private JLabel MatricolaLabel;
    private JPanel SezioneRicerca;
    private JButton ChiudiButton;

    public TrovaImpiegato() {
        setTitle("CitizenServices");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600); // Imposta le dimensioni desiderate (larghezza x altezza)
        setLocationRelativeTo(null);
        setContentPane(TrovaImpiegato);
        setResizable(false);

        ControlGestioneImpiegati CGI = new ControlGestioneImpiegati();
        CGI.ottieniListaImpiegati(listaImpiegati);
        for(Utente utente : listaImpiegati){
            listaFiltrata.add(utente);
        }
        CustomListModel<Utente> CLM = new CustomListModel<>(listaFiltrata);
        TrovaImpiegatoCellRenderer TICR = new TrovaImpiegatoCellRenderer();
        JListaImpiegati.setModel(CLM);
        JListaImpiegati.setCellRenderer(TICR);
        JListaImpiegati.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JListaImpiegati.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = JListaImpiegati.getSelectedIndex();
                Utente utente = listaFiltrata.get(index);
                VisualizzaDettagliImpiegato VDI = new VisualizzaDettagliImpiegato(utente);
                VDI.setVisible(true);
                dispose();
            }
        });

        FiltraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomeInput = NomeInput.getText().toString();
                String cognomeInput = CognomeInput.getText().toString();
                String matricolaInput = MatricolaInput.getText().toString();
                CGI.filtraLista(matricolaInput, nomeInput, cognomeInput, listaImpiegati, listaFiltrata);
                JListaImpiegati.repaint();
            }
        });

        ChiudiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DatiUtenteLoggato DUL = new DatiUtenteLoggato();
                CommonMethods.mostraHomepage(DUL.getUtente().getRuolo());
                dispose();
            }
        });
    }
}
