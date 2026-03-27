package COM.CS.GestioneTurnazione.Interface;

import COM.CS.Commons.AvvisoErrore;
import COM.CS.Entity.Richiesta;
import COM.CS.Entity.Turno;
import COM.CS.GestioneTurnazione.Control.ControlGestioneAssenza;
import COM.CS.GestioneTurnazione.Control.ControlGestioneRichieste;
import COM.CS.GestioneTurnazione.Interface.Renderer.ListaRichiesteCellRenderer;
import COM.CS.Utili.CommonMethods;
import COM.CS.Utili.CustomListModel;
import COM.CS.Utili.DatiUtenteLoggato;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ApprovazioneRichiesta extends JFrame {
    private int selectedIndex = -1;
    private JPanel ApprovazioneRichiesta;
    private JList JListaRichieste;
    private JButton approvaButton;
    private JButton rifiutaButton;
    private JButton chiudiButton;

    public ApprovazioneRichiesta(ArrayList<Richiesta> listaRichieste,ControlGestioneRichieste CGR) {
        setTitle("CitizenServices");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 500); // Imposta le dimensioni desiderate (larghezza x altezza)
        setLocationRelativeTo(null);
        setContentPane(ApprovazioneRichiesta);
        setResizable(false);
        setVisible(true);

        CustomListModel<Richiesta> listModel = new CustomListModel<>(listaRichieste);
        ListaRichiesteCellRenderer renderer = new ListaRichiesteCellRenderer();
        JListaRichieste.setModel(listModel);
        JListaRichieste.setCellRenderer(renderer);
        JListaRichieste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        chiudiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DatiUtenteLoggato DUL = new DatiUtenteLoggato();
                CommonMethods.mostraHomepage(DUL.getUtente().getRuolo());
                dispose();
            }
        });
        rifiutaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedIndex!=-1){
                    Richiesta selezionata = listaRichieste.get(selectedIndex);
                    CGR.registraRifiutoRichiesta(selezionata);
                    listaRichieste.remove(selectedIndex);
                    JListaRichieste.repaint();
                }
            }
        });
        approvaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedIndex!=-1){
                    Richiesta selezionata = listaRichieste.get(selectedIndex);
                    //richiama gestione assenza
                    ArrayList<Turno> turni = new ArrayList<>();
                    ControlGestioneAssenza CGA = new ControlGestioneAssenza(selezionata.getRef_utente(), selezionata.getInizioPeriodo(), selezionata.getFinePeriodo(), turni);
                    CGR.registraApprovazioneRichiesta(selezionata);
                    listaRichieste.remove(selectedIndex);
                    JListaRichieste.repaint();
                }
            }
        });
        JListaRichieste.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectedIndex = JListaRichieste.getSelectedIndex();
            }
        });
    }
}
