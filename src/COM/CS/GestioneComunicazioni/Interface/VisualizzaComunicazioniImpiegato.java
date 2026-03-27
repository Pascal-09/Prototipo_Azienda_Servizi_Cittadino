package COM.CS.GestioneComunicazioni.Interface;

import COM.CS.Commons.AvvisoErrore;
import COM.CS.Entity.Comunicazione;
import COM.CS.GestioneComunicazioni.Control.ControlGestioneComunicazioni;
import COM.CS.GestioneComunicazioni.Interface.Renderer.ComunicazioneCellRenderer;
import COM.CS.Utili.CommonMethods;
import COM.CS.Utili.CustomListModel;
import COM.CS.Utili.DatiUtenteLoggato;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class VisualizzaComunicazioniImpiegato extends JFrame {
    ArrayList<Comunicazione> listaComunicazioniImpiegato = new ArrayList<>();
    private JPanel VisualizzaComunicazioniImpiegato;
    private JScrollPane PannelloScrollabile;
    private JList Lista;
    private JButton ChiudiButton;

    public VisualizzaComunicazioniImpiegato() { //metodo costruttore, equivalente a create()
        setTitle("CitizenServices");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 300); // Imposta le dimensioni desiderate (larghezza x altezza)
        setLocationRelativeTo(null);
        setContentPane(VisualizzaComunicazioniImpiegato);
        setResizable(false);

        DatiUtenteLoggato DUL = new DatiUtenteLoggato();
        ControlGestioneComunicazioni CGC = new ControlGestioneComunicazioni();
        ComunicazioneCellRenderer CCR = new ComunicazioneCellRenderer();
        CGC.ottieniComunicazioniImpiegato(DUL.getUtente().getMatricola(), listaComunicazioniImpiegato);
        if(CGC.isListaComunicazioniVuota(listaComunicazioniImpiegato)){
            //sappiamo che funziona e che restituisce le comunicazioni
            CustomListModel<Comunicazione> CLM = new CustomListModel<>(listaComunicazioniImpiegato);
            Lista.setCellRenderer(CCR);
            Lista.setModel(CLM);

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


        ChiudiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommonMethods.mostraHomepage(DUL.getUtente().getRuolo());
                dispose();
            }
        });
    }
}
