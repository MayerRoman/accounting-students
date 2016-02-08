package gui.dialog.exception;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by Mayer Roman on 03.02.2016.
 */
public class DataBaseInitializingExceptionDialog extends JDialog {
    public DataBaseInitializingExceptionDialog(IOException e) {
        this.setPreferredSize(new Dimension(460, 190));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setModal(true);


        JPanel errorMessagePanel = new JPanel(new GridLayout(2, 1));
        this.add(errorMessagePanel, BorderLayout.CENTER);

        errorMessagePanel.add(new JLabel("Ошибка инициализации базы данных", SwingConstants.CENTER), BorderLayout.CENTER);
        errorMessagePanel.add(new JLabel(e.getMessage(), SwingConstants.CENTER), BorderLayout.CENTER);


        JButton button = new JButton("ОК");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        this.add(button, BorderLayout.SOUTH);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
