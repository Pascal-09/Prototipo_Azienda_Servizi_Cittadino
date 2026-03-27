package COM.CS.Commons;

import javax.swing.*;
import java.awt.event.*;

public class AvvisoSuccesso extends JDialog {
    private JPanel AvvisoSuccesso;
    private JLabel IconaAvvisoSuccesso;
    private JPanel SezioneTesto;
    private JLabel TestoAvvenutaOperazione;
    private JPanel SezioneTasti;
    private JButton ChiudiButton;

    public AvvisoSuccesso(String msg) {
        setTitle("CitizenServices");
        setContentPane(AvvisoSuccesso);
        setModal(true);
        TestoAvvenutaOperazione.setText(msg);
        getRootPane().setDefaultButton(ChiudiButton);
        ChiudiButton.addActionListener(new ActionListener() {
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
        AvvisoSuccesso.registerKeyboardAction(new ActionListener() {
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

    public static void main(String[] args) {
        AvvisoSuccesso dialog = new AvvisoSuccesso("messaggio");
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
