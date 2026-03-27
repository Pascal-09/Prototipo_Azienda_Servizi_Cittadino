package COM.CS.GestioneComunicazioni.Interface.Renderer;

import COM.CS.Entity.Servizio;
import COM.CS.Entity.Utente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ImpiegatiServiziCellRenderer extends JPanel implements ListCellRenderer<Utente>{
    private JLabel NomeLabel;
    private JLabel CognomeLabel;
    private JLabel MatricolaLabel;
    public ImpiegatiServiziCellRenderer(){
        setLayout(new BorderLayout());

        setBorder(new LineBorder(Color.BLACK));

        NomeLabel = new JLabel();
        CognomeLabel = new JLabel();
        MatricolaLabel = new JLabel();

        NomeLabel.setBorder(new EmptyBorder(5,0,5,0));
        CognomeLabel.setBorder(new EmptyBorder(5,0,5,0));
        MatricolaLabel.setBorder(new EmptyBorder(5,0,5,0));

        add(NomeLabel, BorderLayout.NORTH);
        add(CognomeLabel, BorderLayout.CENTER);
        add(MatricolaLabel, BorderLayout.SOUTH);
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
