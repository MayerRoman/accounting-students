package gui.dialog.group;

import dao.GroupDAO;
import dto.Group;
import dto.Student;
import gui.dialog.exception.DataBaseExceptionDialog;
import gui.tab.GroupTab;
import gui.tab.StudentTab;

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
public class GroupChangeDialog extends JDialog {
    private JTextField textFieldGroupNumber;
    private JTextField textFieldFaculty;
    private Group editableGroup;
    private int selectedRowInTable;
    private GroupDAO groupDAO;

    public GroupChangeDialog(GroupDAO groupDAO) {
        this.groupDAO = groupDAO;

        this.setPreferredSize(new Dimension(580, 250));
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setModal(true);

        this.add(createPanelWithTextFields(), BorderLayout.CENTER);
        this.add(createPanelWithButtons(), BorderLayout.SOUTH);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }


    private JPanel createPanelWithTextFields() {
        JPanel mainPanel = new JPanel(new GridLayout(2, 1));

        int selectedRowInView = GroupTab.groupTable.getSelectedRow();
        selectedRowInTable = GroupTab.groupTable.convertRowIndexToModel(selectedRowInView);
        editableGroup = GroupTab.groups.get(selectedRowInTable);


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
        textFieldGroupNumber.setText(Integer.toString(editableGroup.getGroupNumber()));

        SpringLayout springLayout1 = new SpringLayout();
        springLayout1.putConstraint(SpringLayout.EAST, numberLabel, -20, SpringLayout.WEST, textFieldGroupNumber);
        springLayout1.putConstraint(SpringLayout.NORTH, numberLabel, 35, SpringLayout.NORTH, groupNumberPanel);
        springLayout1.putConstraint(SpringLayout.WEST, textFieldGroupNumber, 125,SpringLayout.WEST, groupNumberPanel);
        springLayout1.putConstraint(SpringLayout.NORTH, textFieldGroupNumber, 35,SpringLayout.NORTH, groupNumberPanel);
        groupNumberPanel.setLayout(springLayout1);


        JPanel facultyPanel = new JPanel();
        mainPanel.add(facultyPanel);

        JLabel facultyLabel = new JLabel("Факультет");
        facultyPanel.add(facultyLabel);

        textFieldFaculty = new JTextField(editableGroup.getGroupFaculty(), 30);
        facultyPanel.add(textFieldFaculty);

        SpringLayout springLayout2 = new SpringLayout();
        springLayout2.putConstraint(SpringLayout.EAST, facultyLabel, -20, SpringLayout.WEST, textFieldFaculty);
        springLayout2.putConstraint(SpringLayout.NORTH, facultyLabel, 25, SpringLayout.NORTH, facultyPanel);
        springLayout2.putConstraint(SpringLayout.WEST, textFieldFaculty, 125,SpringLayout.WEST, facultyPanel);
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

                    boolean enteredNewGroupNumber = false;
                    int enteredGroupNumber = Integer.parseInt(textFieldGroupNumber.getText());
                    if (editableGroup.getGroupNumber() != enteredGroupNumber) {
                        enteredNewGroupNumber = true;
                    }

                    if (enteredNewGroupNumber) {

                        boolean groupExists = false;
                        for (Group group : GroupTab.groups) {
                            if (group.getGroupNumber() == enteredGroupNumber) {
                                groupExists = true;
                            }
                        }

                        if (!groupExists) {
                            final JDialog dialog = new JDialog();
                            dialog.setModal(true);
                            dialog.setPreferredSize(new Dimension(500, 150));
                            dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

                            JLabel label = new JLabel("Номер группы будет также изменён в записях студентов", SwingConstants.CENTER);
                            dialog.add(label, BorderLayout.CENTER);

                            JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
                            dialog.add(buttonPanel, BorderLayout.SOUTH);

                            JButton okButton = new JButton("OK");
                            okButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {

                                    int previousGroupNumber = editableGroup.getGroupNumber();

                                    Group tempGroup = new Group();

                                    tempGroup.setGroupId(editableGroup.getGroupId());
                                    tempGroup.setGroupNumber(Integer.parseInt(textFieldGroupNumber.getText()));
                                    tempGroup.setGroupFaculty(textFieldFaculty.getText());

                                    try {
                                        groupDAO.updateGroup(tempGroup);

                                        editableGroup.setGroupNumber(tempGroup.getGroupNumber());
                                        editableGroup.setGroupFaculty(tempGroup.getGroupFaculty());
                                        GroupTab.groupTableModel.fireTableDataChanged();

                                        for (Student student : StudentTab.students) {
                                            if (student.getStudentGroupNumber() == previousGroupNumber) {
                                                student.setStudentGroupNumber(editableGroup.getGroupNumber());
                                            }
                                        }
                                        StudentTab.studentTableModel.fireTableDataChanged();
                                    } catch (SQLException e1) {
                                        new DataBaseExceptionDialog(e1);
                                    }

                                    dispose();
                                    dialog.dispose();
                                }
                            });
                            buttonPanel.add(okButton);

                            JButton cancelButton = new JButton("Отмена");
                            cancelButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    dialog.dispose();
                                }
                            });
                            buttonPanel.add(cancelButton);

                            dialog.pack();
                            dialog.setLocationRelativeTo(null);
                            dialog.setVisible(true);
                        }

                        else {
                            final JDialog dialog = new JDialog();
                            dialog.setModal(true);
                            dialog.setPreferredSize(new Dimension(270, 140));
                            dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

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
                        editableGroup.setGroupFaculty(textFieldFaculty.getText());

                        try {
                            groupDAO.updateGroup(editableGroup);
                        } catch (SQLException e1) {
                            new DataBaseExceptionDialog(e1);
                        }

                        GroupTab.groupTableModel.fireTableDataChanged();
                        dispose();
                    }
                }

                else {
                    final JDialog dialog = new JDialog();
                    dialog.setModal(true);
                    dialog.setPreferredSize(new Dimension(290, 150));
                    dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

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
