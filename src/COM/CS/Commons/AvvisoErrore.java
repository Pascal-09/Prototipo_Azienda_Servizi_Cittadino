package COM.CS.Commons;

import javax.swing.*;
import java.awt.event.*;

public class AvvisoErrore extends JDialog {
    private JPanel AvvisoErrore;
    private JButton chiudiButton;
    private JPanel SezioneTasti;
    private JLabel IconaAvvisoErrore;
    private JPanel SezioneTesto;
    private JLabel TestoErroreGenerico;

    public AvvisoErrore(String msg) {
        setTitle("CitizenServices");
        setContentPane(AvvisoErrore);
        TestoErroreGenerico.setText(msg);
        setModal(true);
        getRootPane().setDefaultButton(chiudiButton);

        chiudiButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        AvvisoErrore.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public void changeButtonText(String txt){
        chiudiButton.setText(txt);
    }

    public static void main(String[] args) {
        AvvisoErrore dialog = new AvvisoErrore("errore generico");
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
