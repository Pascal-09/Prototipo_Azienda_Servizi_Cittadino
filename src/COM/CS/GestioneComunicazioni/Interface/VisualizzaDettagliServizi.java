package COM.CS.GestioneComunicazioni.Interface;

import COM.CS.Commons.AvvisoErrore;
import COM.CS.Entity.Servizio;
import COM.CS.Entity.Utente;
import COM.CS.GestioneComunicazioni.Control.ControlGestioneComunicazioni;
import COM.CS.GestioneComunicazioni.Interface.Renderer.ImpiegatiServiziCellRenderer;
import COM.CS.GestioneComunicazioni.Interface.Renderer.ServizioCellRenderer;
import COM.CS.Utili.CommonMethods;
import COM.CS.Utili.CustomListModel;
import COM.CS.Utili.DatiUtenteLoggato;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class VisualizzaDettagliServizi extends JFrame{
    ArrayList<Servizio> listaInfoServizi = new ArrayList<>();
    ArrayList<Utente> listaImpiegati = new ArrayList<>();
    private JPanel VisualizzaDettagliServizi;
    private JButton Chiudi;
    private JList Lista;
    private JScrollPane PannelloScrollabile;
    private JList ListaImpiegati;

    public VisualizzaDettagliServizi(){
        setTitle("CitizenServices");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 500); // Imposta le dimensioni desiderate (larghezza x altezza)
        setLocationRelativeTo(null);
        setContentPane(VisualizzaDettagliServizi);
        setResizable(false);

        ControlGestioneComunicazioni CGC = new ControlGestioneComunicazioni();
        CGC.aggiornaServiziAttivi();
        CGC.aggiornaServiziChiusi();
        ServizioCellRenderer SCR = new ServizioCellRenderer();
        CGC.ottieniDettagliServizi(listaInfoServizi); //riempimento lista Servizi (ovvero, stiamo facendo la query)
        if(!listaInfoServizi.isEmpty()){ //controlliamo se la lista è vuota
            //RIEMPIMENTO LISTA SUL FORM
            CustomListModel<Servizio> CLM = new CustomListModel<>(listaInfoServizi);
            Lista.setCellRenderer(SCR); //decide l'aspetto delle singole celle del JPanel
            Lista.setModel(CLM); //decide alcune proprietà complessive della lista nel JPanel

            Lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            Lista.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (Lista.getSelectedValue() != null) {
                        Servizio selectedServizio = (Servizio) Lista.getSelectedValue();
                        CGC.ottieniImpiegatiAssegnatiAlServizio(selectedServizio.getCod_servizio(), listaImpiegati);
                        if(selectedServizio.getStato().equals("chiuso") || listaImpiegati.isEmpty()){
                            ListaImpiegati.repaint();
                            AvvisoErrore AE = new AvvisoErrore("il servizio è stato chiuso!");
                            AE.pack();
                            AE.setVisible(true);
                        }
                        else{
                            CustomListModel<Utente> CLMImpiegati = new CustomListModel<>(listaImpiegati);
                            ImpiegatiServiziCellRenderer ISCR = new ImpiegatiServiziCellRenderer();
                            ListaImpiegati.setCellRenderer(ISCR);
                            ListaImpiegati.setModel(CLMImpiegati);
                        }
                    }
                }
            });
        }
        else{
            AvvisoErrore AE = new AvvisoErrore("non ci sono comunicazioni");
            AE.pack();
            AE.setVisible(true);
        }

        setVisible(true);

        Chiudi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DatiUtenteLoggato DUL = new DatiUtenteLoggato();
                CommonMethods.mostraHomepage(DUL.getUtente().getRuolo());
                dispose();
            }
        });
    }
}
