package COM.CS.GestioneStipendio.Interface;

import COM.CS.Commons.AvvisoErrore;
import COM.CS.Commons.AvvisoSuccesso;
import COM.CS.Entity.Stipendio;
import COM.CS.Entity.Utente;
import COM.CS.GestioneStipendio.Control.ControlGestioneStipendio;
import COM.CS.GestioneStipendio.Interface.Renderer.StipendioCellRenderer;
import COM.CS.Utili.CommonMethods;
import COM.CS.Utili.CustomListModel;
import COM.CS.Utili.DatiUtenteLoggato;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AccreditaStipendio extends JFrame{
    private JPanel AccreditaStipendio;
    private JList JListaStipendi;
    private JButton ConfermaAccreditoButton;
    private JButton chiudiButton;

    public AccreditaStipendio(ArrayList<Stipendio> stipendi, ArrayList<Utente> impiegati, ControlGestioneStipendio CGS){
        setTitle("CitizenServices");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 600); // Imposta le dimensioni desiderate (larghezza x altezza)
        setLocationRelativeTo(null);
        setContentPane(AccreditaStipendio);
        setResizable(false);

        DatiUtenteLoggato DUL = new DatiUtenteLoggato();
        CustomListModel<Stipendio> customListModel = new CustomListModel<>(stipendi);
        StipendioCellRenderer renderer = new StipendioCellRenderer();
        JListaStipendi.setModel(customListModel);
        JListaStipendi.setCellRenderer(renderer);
        JListaStipendi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ConfermaAccreditoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(Stipendio stipendio : stipendi){
                   for(Utente impiegato : impiegati){
                       if(stipendio.getRef_matricola().equals(impiegato.getMatricola())){
                           CGS.aggiornaStatoStipendio(stipendio.getRef_matricola(), impiegato.getIBAN());
                       }
                   }
                }
                dispose();
                CommonMethods.mostraHomepage(DUL.getUtente().getRuolo());
                AvvisoSuccesso AS = new AvvisoSuccesso("Tutti gli stipendi sono stati accreditati con successo");
                AS.pack();
                AS.setVisible(true);
            }
        });
        chiudiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommonMethods.mostraHomepage(DUL.getUtente().getRuolo());
                dispose();
            }
        });
    }
}
