package COM.CS.GestioneContratti.Interface.Renderer;


import COM.CS.Entity.Servizio;
import COM.CS.Entity.Utente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TrovaImpiegatoCellRenderer extends JPanel implements ListCellRenderer<Utente>{
    private JLabel NomeLabel;
    private JLabel CognomeLabel;
    private JLabel MatricolaLabel;
    public JButton EsaminaDettagliImpiegato;

    public TrovaImpiegatoCellRenderer(){
        setLayout(new BorderLayout());

        setBorder(new LineBorder(Color.BLACK));

        NomeLabel = new JLabel();
        CognomeLabel = new JLabel();
        MatricolaLabel = new JLabel();
        EsaminaDettagliImpiegato = new JButton();

        NomeLabel.setBorder(new EmptyBorder(5,5,5,0));
        CognomeLabel.setBorder(new EmptyBorder(5,5,5,5));
        MatricolaLabel.setBorder(new EmptyBorder(5,5,5,5));
        EsaminaDettagliImpiegato.setBorder(new EmptyBorder(10, 5, 5, 5));
        EsaminaDettagliImpiegato.setText("Esamina Dettagli Impiegato");

        EsaminaDettagliImpiegato.setBackground(new Color(0,123,255));
        EsaminaDettagliImpiegato.setForeground(new Color(255,255,255));

        add(NomeLabel, BorderLayout.NORTH);
        add(CognomeLabel, BorderLayout.CENTER);
        add(MatricolaLabel, BorderLayout.SOUTH);
        add(EsaminaDettagliImpiegato, BorderLayout.EAST);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Utente> list, Utente value, int index, boolean isSelected, boolean cellHasFocus) {
        if(value!=null){
            NomeLabel.setText("Nome: " + value.getNome());
            CognomeLabel.setText("Cognome: " + value.getCognome());
            MatricolaLabel.setText("Matricola: " + value.getMatricola());
        }
        if(isSelected){
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        }
        else{
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        return this;
    }

}
