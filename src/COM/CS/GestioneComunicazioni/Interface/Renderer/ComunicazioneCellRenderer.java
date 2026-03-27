package COM.CS.GestioneComunicazioni.Interface.Renderer;

import COM.CS.Entity.Comunicazione;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ComunicazioneCellRenderer extends JPanel implements ListCellRenderer<Comunicazione>{

    private JLabel idLabel;
    private JLabel orarioLabel;
    private JLabel dataLabel;
    private JButton mostraComunicazioneButton;

    public ComunicazioneCellRenderer() {
        setLayout(new BorderLayout()); //crea la cella **

        setBorder(new LineBorder(Color.BLACK)); //setta il bordino, per separare le celle nella lista

        //inizializzazione  oggetti della cella **
        idLabel = new JLabel();
        orarioLabel = new JLabel();
        dataLabel = new JLabel();
        mostraComunicazioneButton = new JButton("Mostra comunicazione");

        //decorazione oggetti cella
        idLabel.setBorder(new EmptyBorder(5, 5, 10, 5));
        orarioLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        dataLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        mostraComunicazioneButton.setBackground(new Color(0, 123,255));
        mostraComunicazioneButton.setForeground(new Color(225,225,225));

        Font labelFont = idLabel.getFont();
        idLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 18));
        orarioLabel.setFont(new Font(labelFont.getName(), Font.PLAIN, 14));  // You can adjust the size (14) as needed
        dataLabel.setFont(new Font(labelFont.getName(), Font.PLAIN, 14));  // You can adjust the size (14) as needed

        //aggiungiamo alla grafica per mostrarli **
        add(idLabel, BorderLayout.NORTH);
        add(orarioLabel, BorderLayout.CENTER);
        add(dataLabel, BorderLayout.SOUTH);
        add(mostraComunicazioneButton, BorderLayout.EAST);

    }
    @Override
    public Component getListCellRendererComponent(JList<? extends Comunicazione> list, Comunicazione value, int index, boolean isSelected, boolean cellHasFocus) { //**
        if (value != null) {
            //utilizzo delle info dell'oggetto della lista per mostrarle a video
            String[] parts = value.getOrarioInvio().split(" ");
            String orario = parts[1];
            String data = parts[0];
            idLabel.setText("Comunicazione " + value.getCod_comunicazione());
            orarioLabel.setText("Orario Ricezione: " + orario);
            dataLabel.setText("Data: " + data);
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
