package COM.CS.GestioneTurnazione.Interface;

import COM.CS.Entity.Turno;
import COM.CS.Entity.Utente;
import COM.CS.GestioneTurnazione.Control.ControlGestioneTurnazione;
import COM.CS.GestioneTurnazione.Interface.Renderer.ListaImpiegatiTurnoSettimanaCellRenderer;
import COM.CS.Utili.CommonMethods;
import COM.CS.Utili.CustomListModel;
import COM.CS.Utili.DatiUtenteLoggato;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VisualizzaTurnazioneDegliImpiegati extends JFrame{
    private JPanel VisualizzaTurnazioneDegliImpiegati;
    private JList ListaImpiegatiTurniSettimana;
    private JButton ChiudiButton;
    private JLabel infoVisualizzazione;
    private JList ListaTurnazioneSettimanale;
    private JLabel titoloTurnazioneImpiegato;

    public VisualizzaTurnazioneDegliImpiegati(ArrayList<Utente> listaImpiegatiTurniSettimanali, ControlGestioneTurnazione CGT){
        setTitle("CitizenServices");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 500); // Imposta le dimensioni desiderate (larghezza x altezza)
        setLocationRelativeTo(null);
        setContentPane(VisualizzaTurnazioneDegliImpiegati);
        setResizable(false);
        setVisible(true);

        CustomListModel<Utente> CLM = new CustomListModel<>(listaImpiegatiTurniSettimanali);
        ListaImpiegatiTurnoSettimanaCellRenderer LITSCR = new ListaImpiegatiTurnoSettimanaCellRenderer();
        ListaImpiegatiTurniSettimana.setModel(CLM);
        ListaImpiegatiTurniSettimana.setCellRenderer(LITSCR);
        ListaImpiegatiTurniSettimana.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListaTurnazioneSettimanale.setVisible(false);
        titoloTurnazioneImpiegato.setVisible(false);
        infoVisualizzazione.setVisible(false);
        ListaImpiegatiTurniSettimana.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //ottengo l'impiegato selezionato
                int index = ListaImpiegatiTurniSettimana.getSelectedIndex();
                Utente impiegato = listaImpiegatiTurniSettimanali.get(index);
                ArrayList<Turno> listaTurniSettimanaliImpiegato = new ArrayList<>();
                infoVisualizzazione.setText("Stai visualizzando i turni settimanali dell'impiegato " + impiegato.getNome() + " " + impiegato.getCognome() + " (Matricola: "+ impiegato.getMatricola() + ").");
                ListaTurnazioneSettimanale.setVisible(true);
                titoloTurnazioneImpiegato.setVisible(true);
                infoVisualizzazione.setVisible(true);
                //otteniamo i turni dell'impiegato selezionato
                CGT.ottieniTurniSettimanali(impiegato.getMatricola(), listaTurniSettimanaliImpiegato);
                //mostriamo i turni sulla lista
                DefaultListModel<String> listModel = new DefaultListModel<>();
                ListaTurnazioneSettimanale.setModel(listModel);
                for(Turno turno : listaTurniSettimanaliImpiegato){
                    listModel.addElement(turno.toString());
                }
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
