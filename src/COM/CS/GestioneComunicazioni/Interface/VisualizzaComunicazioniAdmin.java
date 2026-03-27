package COM.CS.GestioneComunicazioni.Interface;

import COM.CS.Commons.AvvisoErrore;
import COM.CS.GestioneComunicazioni.Interface.Renderer.ComunicazioneCellRenderer;
import COM.CS.Utili.CommonMethods;
import COM.CS.Utili.CustomListModel;
import COM.CS.Utili.DatiUtenteLoggato;
import COM.CS.Entity.Comunicazione;
import COM.CS.GestioneComunicazioni.Control.ControlGestioneComunicazioni;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class VisualizzaComunicazioniAdmin extends JFrame {
    ArrayList<Comunicazione> listaComunicazioniAdmin = new ArrayList<>();
    private JPanel VisualizzaComunicazioniAdmin;
    private JList Lista;
    private JButton chiudiButton;
    private JPanel SezioneTasti;
    private JScrollPane PannelloScrollabile;

    public VisualizzaComunicazioniAdmin() { //metodo costruttore, equivalente a create()
        setTitle("CitizenServices");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 300); // Imposta le dimensioni desiderate (larghezza x altezza)
        setLocationRelativeTo(null);
        setContentPane(VisualizzaComunicazioniAdmin);
        setResizable(false);

        DatiUtenteLoggato DUL = new DatiUtenteLoggato();
        ControlGestioneComunicazioni CGC = new ControlGestioneComunicazioni();
        ComunicazioneCellRenderer CCR = new ComunicazioneCellRenderer();
        CGC.ottieniComunicazioniAdmin(DUL.getUtente().getMatricola(), listaComunicazioniAdmin); //riempimento lista comunicazioni (ovvero, stiamo facendo la query)
        if(CGC.isListaComunicazioniVuota(listaComunicazioniAdmin)){ //controlliamo se la lista è vuota
            //RIEMPIMENTO LISTA SUL FORM
            CustomListModel<Comunicazione> CLM = new CustomListModel<>(listaComunicazioniAdmin);
            Lista.setCellRenderer(CCR); //decide l'aspetto delle singole celle del JPanel
            Lista.setModel(CLM); //decide alcune proprietà complessive della lista nel JPanel

            Lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            Lista.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (Lista.getSelectedValue() != null) {
                        Comunicazione selectedComunicazione = (Comunicazione) Lista.getSelectedValue();
                        MostraComunicazione MC = new MostraComunicazione(selectedComunicazione);
                        MC.setVisible(true);
                        dispose();
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


        chiudiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommonMethods.mostraHomepage(DUL.getUtente().getRuolo());
                dispose();
            }
        });
    }
}
