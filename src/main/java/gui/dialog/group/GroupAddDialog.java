package gui.dialog.group;

import dao.GroupDAO;
import dto.Group;
import gui.dialog.exception.DataBaseExceptionDialog;
import gui.tab.GroupTab;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Created by Mayer Roman on 20.01.2016.
 */
public class GroupAddDialog extends JDialog {
    private JTextField textFieldGroupNumber;
    private JTextField textFieldFaculty;
    private GroupDAO groupDAO;

    public GroupAddDialog(GroupDAO groupDAO) {
        this.groupDAO = groupDAO;

        this.setPreferredSize(new Dimension(580, 250));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setModal(true);

        this.add(createPanelWithTextFields(), BorderLayout.CENTER);
        this.add(createPanelWithButtons(), BorderLayout.SOUTH);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public GroupAddDialog(GroupDAO groupDAO, String groupNumber) {
        this.groupDAO = groupDAO;

        this.setPreferredSize(new Dimension(580, 250));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setModal(true);

        this.add(createPanelWithTextFields(), BorderLayout.CENTER);
        textFieldGroupNumber.setText(groupNumber);

        this.add(createPanelWithButtons(), BorderLayout.SOUTH);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }


    private JPanel createPanelWithTextFields() {
        JPanel mainPanel = new JPanel(new GridLayout(2, 1));


        JPanel groupNumberPanel = new JPanel();
        mainPanel.add(groupNumberPanel);

        JLabel numberLabel = new JLabel("Номер");
        groupNumberPanel.add(numberLabel);

        textFieldGroupNumber = new JTextField(30);
        groupNumberPanel.add(textFieldGroupNumber);

        PlainDocument plainDocument = new PlainDocument();
        plainDocument.setDocumentFilter(new DocumentFilter(){
            public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr)
                    throws BadLocationException {
                if (checkString(string)) {
                    super.replace(fb, offset, length, string, attr);
                }
            }

            private boolean checkString(String s) {
                if (s != null) {
                    for (int i = 0; i < s.length(); i++) {
                        if (!Character.isDigit(s.charAt(i))) {
                            return false;
                        }
                    }
                }
                return true;
            }
        });
        textFieldGroupNumber.setDocument(plainDocument);

        SpringLayout springLayout1 = new SpringLayout();
        springLayout1.putConstraint(SpringLayout.EAST, numberLabel, -20, SpringLayout.WEST, textFieldGroupNumber);
        springLayout1.putConstraint(SpringLayout.NORTH, numberLabel, 35, SpringLayout.NORTH, groupNumberPanel);
        springLayout1.putConstraint(SpringLayout.WEST, textFieldGroupNumber, 115,SpringLayout.WEST, groupNumberPanel);
        springLayout1.putConstraint(SpringLayout.NORTH, textFieldGroupNumber, 35,SpringLayout.NORTH, groupNumberPanel);
        groupNumberPanel.setLayout(springLayout1);


        JPanel facultyPanel = new JPanel();
        mainPanel.add(facultyPanel);

        JLabel facultyLabel = new JLabel("Факультет");
        facultyPanel.add(facultyLabel);

        textFieldFaculty = new JTextField(30);
        facultyPanel.add(textFieldFaculty);

        SpringLayout springLayout2 = new SpringLayout();
        springLayout2.putConstraint(SpringLayout.EAST, facultyLabel, -20, SpringLayout.WEST, textFieldFaculty);
        springLayout2.putConstraint(SpringLayout.NORTH, facultyLabel, 25, SpringLayout.NORTH, facultyPanel);
        springLayout2.putConstraint(SpringLayout.WEST, textFieldFaculty, 115,SpringLayout.WEST, facultyPanel);
        springLayout2.putConstraint(SpringLayout.NORTH, textFieldFaculty, 25,SpringLayout.NORTH, facultyPanel);
        facultyPanel.setLayout(springLayout2);


        return mainPanel;
    }

    private JPanel createPanelWithButtons() {
        JPanel panel = new JPanel(new GridLayout(1, 3));

        JButton okButton = new JButton("ОК");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (textFieldGroupNumber.getText().trim().length() > 0 && textFieldFaculty.getText().trim().length() > 0) {

                    boolean groupExists = false;
                    int groupNumber = Integer.parseInt(textFieldGroupNumber.getText());
                    for (Group group : GroupTab.groups) {
                        if (group.getGroupNumber() == groupNumber) {
                            groupExists = true;
                        }
                    }

                    if (!groupExists) {
                        Group group = new Group(Integer.parseInt(textFieldGroupNumber.getText()), textFieldFaculty.getText());

                        try {
                            long groupId = groupDAO.createGroup(group);
                            group.setGroupId(groupId);
                            GroupTab.groups.add(group);
                            GroupTab.groupTableModel.fireTableDataChanged();
                        } catch (SQLException e1) {
                            new DataBaseExceptionDialog(e1);
                        }

                        dispose();
                    }

                    else {
                        final JDialog dialog = new JDialog();
                        dialog.setModal(true);
                        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                        dialog.setPreferredSize(new Dimension(270, 140));

                        JLabel label = new JLabel("Группа уже существует", SwingConstants.CENTER);
                        dialog.add(label, BorderLayout.CENTER);

                        JButton button = new JButton("ОК");
                        button.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                dialog.dispose();
                            }
                        });
                        dialog.add(button, BorderLayout.SOUTH);

                        dialog.pack();
                        dialog.setLocationRelativeTo(null);
                        dialog.setVisible(true);
                    }
                }

                else {
                    final JDialog dialog = new JDialog();
                    dialog.setModal(true);
                    dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                    dialog.setPreferredSize(new Dimension(290, 150));

                    dialog.add(new JLabel("Необходимо заполнить все поля", SwingConstants.CENTER), BorderLayout.CENTER);

                    JButton button = new JButton("OK");
                    button.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            dialog.dispose();
                        }
                    });
                    dialog.add(button, BorderLayout.SOUTH);


                    dialog.pack();
                    dialog.setLocationRelativeTo(null);
                    dialog.setVisible(true);
                }
            }
        });
        panel.add(okButton);

        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        panel.add(cancelButton);

        return panel;
    }
}
