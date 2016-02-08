package gui.dialog.student;

import dao.GroupDAO;
import dao.StudentDAO;
import dto.Group;
import dto.Student;
import gui.dialog.exception.DataBaseExceptionDialog;
import gui.dialog.group.GroupAddDialog;
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
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * Created by Mayer Roman on 20.01.2016.
 */
public class StudentAddDialog extends JDialog {
    private JTextField textFieldName;
    private JTextField textFieldSurname;
    private JTextField textFieldPatronymic;
    private JSpinner spinnerDate;
    private JTextField textFieldGroupNumber;
    private GroupDAO groupDAO;
    private StudentDAO studentDAO;

    public StudentAddDialog(GroupDAO groupDAO, StudentDAO studentDAO) {
        this.groupDAO = groupDAO;
        this.studentDAO = studentDAO;

        this.setPreferredSize(new Dimension(610, 440));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setModal(true);

        this.add(createPanelWithTextFields(), BorderLayout.CENTER);
        this.add(createPanelWithButtons(), BorderLayout.SOUTH);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private JPanel createPanelWithTextFields() {
        JPanel mainPanel = new JPanel(new GridLayout(5, 1));


        JPanel studentNamePanel = new JPanel();
        mainPanel.add(studentNamePanel);

        JLabel nameLabel = new JLabel("Имя");
        studentNamePanel.add(nameLabel);

        textFieldName = new JTextField(30);
        studentNamePanel.add(textFieldName);

        SpringLayout springLayout1 = new SpringLayout();
        springLayout1.putConstraint(SpringLayout.EAST, nameLabel, -10, SpringLayout.WEST, textFieldName);
        springLayout1.putConstraint(SpringLayout.NORTH, nameLabel, 20, SpringLayout.NORTH, studentNamePanel);
        springLayout1.putConstraint(SpringLayout.WEST, textFieldName, 145, SpringLayout.WEST, studentNamePanel);
        springLayout1.putConstraint(SpringLayout.NORTH, textFieldName, 20, SpringLayout.NORTH, studentNamePanel);
        studentNamePanel.setLayout(springLayout1);


        JPanel studentSurnamePanel = new JPanel();
        mainPanel.add(studentSurnamePanel);

        JLabel surnameLabel = new JLabel("Фамилия");
        studentSurnamePanel.add(surnameLabel);

        textFieldSurname = new JTextField(30);
        studentSurnamePanel.add(textFieldSurname);

        SpringLayout springLayout2 = new SpringLayout();
        springLayout2.putConstraint(SpringLayout.EAST, surnameLabel, -10, SpringLayout.WEST, textFieldSurname);
        springLayout2.putConstraint(SpringLayout.NORTH, surnameLabel, 20, SpringLayout.NORTH, studentSurnamePanel);
        springLayout2.putConstraint(SpringLayout.WEST, textFieldSurname, 145, SpringLayout.WEST, studentSurnamePanel);
        springLayout2.putConstraint(SpringLayout.NORTH, textFieldSurname, 20, SpringLayout.NORTH, studentSurnamePanel);
        studentSurnamePanel.setLayout(springLayout2);


        JPanel studentPatronymicPanel = new JPanel();
        mainPanel.add(studentPatronymicPanel);

        JLabel patronymicLabel = new JLabel("Отчество");
        studentPatronymicPanel.add(patronymicLabel);

        textFieldPatronymic = new JTextField(30);
        studentPatronymicPanel.add(textFieldPatronymic);

        SpringLayout springLayout3 = new SpringLayout();
        springLayout3.putConstraint(SpringLayout.EAST, patronymicLabel, -10, SpringLayout.WEST, textFieldPatronymic);
        springLayout3.putConstraint(SpringLayout.NORTH, patronymicLabel, 20, SpringLayout.NORTH, studentPatronymicPanel);
        springLayout3.putConstraint(SpringLayout.WEST, textFieldPatronymic, 145, SpringLayout.WEST, studentPatronymicPanel);
        springLayout3.putConstraint(SpringLayout.NORTH, textFieldPatronymic, 20, SpringLayout.NORTH, studentPatronymicPanel);
        studentPatronymicPanel.setLayout(springLayout3);


        JPanel studentDateOfBirthPanel = new JPanel();
        mainPanel.add(studentDateOfBirthPanel);

        JLabel dateOfBirthLabel = new JLabel("Дата рождения");
        studentDateOfBirthPanel.add(dateOfBirthLabel);

        spinnerDate = createDateSpinner();
        studentDateOfBirthPanel.add(spinnerDate);

        SpringLayout springLayout4 = new SpringLayout();
        springLayout4.putConstraint(SpringLayout.EAST, dateOfBirthLabel, -10, SpringLayout.WEST, spinnerDate);
        springLayout4.putConstraint(SpringLayout.NORTH, dateOfBirthLabel, 20, SpringLayout.NORTH, studentDateOfBirthPanel);
        springLayout4.putConstraint(SpringLayout.WEST, spinnerDate, 145, SpringLayout.WEST, studentDateOfBirthPanel);
        springLayout4.putConstraint(SpringLayout.NORTH, spinnerDate, 20, SpringLayout.NORTH, studentDateOfBirthPanel);
        studentDateOfBirthPanel.setLayout(springLayout4);


        JPanel studentGroupPanel = new JPanel();
        mainPanel.add(studentGroupPanel);

        JLabel groupNumberLabel = new JLabel("Группа");
        studentGroupPanel.add(groupNumberLabel);

        textFieldGroupNumber = new JTextField(30);
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
        studentGroupPanel.add(textFieldGroupNumber);

        SpringLayout springLayout5 = new SpringLayout();
        springLayout5.putConstraint(SpringLayout.EAST, groupNumberLabel, -10, SpringLayout.WEST, textFieldGroupNumber);
        springLayout5.putConstraint(SpringLayout.NORTH, groupNumberLabel, 20, SpringLayout.NORTH, studentGroupPanel);
        springLayout5.putConstraint(SpringLayout.WEST, textFieldGroupNumber, 145, SpringLayout.WEST, studentGroupPanel);
        springLayout5.putConstraint(SpringLayout.NORTH, textFieldGroupNumber, 20, SpringLayout.NORTH, studentGroupPanel);
        studentGroupPanel.setLayout(springLayout5);


        return mainPanel;
    }
    private JSpinner createDateSpinner() {
        JSpinner spinner = new JSpinner();

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.YEAR, -20);
        java.util.Date initDate = calendar.getTime();

        calendar.add(Calendar.YEAR, -100);
        java.util.Date earliestDate = calendar.getTime();

        calendar.add(Calendar.YEAR, 200);
        java.util.Date latestDate = calendar.getTime();

        SpinnerModel spinnerModel = new SpinnerDateModel(initDate, earliestDate, latestDate, Calendar.YEAR);
        spinner.setModel(spinnerModel);

        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));

        return spinner;
    }

    private JPanel createPanelWithButtons() {
        JPanel panel = new JPanel(new GridLayout(1, 3));

        JButton okButton = new JButton("ОК");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (textFieldName.getText().trim().length()>0 && textFieldSurname.getText().trim().length()>0 && textFieldPatronymic.getText().trim().length()>0 && textFieldGroupNumber.getText().trim().length()>0) {

                    boolean groupExist = false;
                    int groupNumber = Integer.parseInt(textFieldGroupNumber.getText());
                    for (Group group : GroupTab.groups) {
                        if (group.getGroupNumber() == groupNumber) {
                            groupExist = true;
                        }
                    }

                    if (groupExist) {
                        Student student = new Student(textFieldName.getText(), textFieldSurname.getText(), textFieldPatronymic.getText(), new Date(((java.util.Date) spinnerDate.getValue()).getTime()), Integer.parseInt(textFieldGroupNumber.getText()));

                        try {
                            long l = studentDAO.createStudent(student);
                            student.setStudentId(l);
                            StudentTab.students.add(student);
                            StudentTab.studentTableModel.fireTableDataChanged();
                        } catch (SQLException e1) {
                            new DataBaseExceptionDialog(e1);
                        }

                        dispose();
                    }

                    else {
                        final JDialog dialog = new JDialog();
                        dialog.setPreferredSize(new Dimension(430, 190));
                        dialog.setModal(true);
                        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

                        dialog.add(new JLabel("Такой группы не существует, создать группу?", SwingConstants.CENTER), BorderLayout.CENTER);


                        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
                        dialog.add(buttonPanel, BorderLayout.SOUTH);

                        JButton okButton = new JButton("OK");
                        okButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                new GroupAddDialog(groupDAO, textFieldGroupNumber.getText());
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
                }

                else {
                    final JDialog dialog = new JDialog();
                    dialog.setPreferredSize(new Dimension(320, 190));
                    dialog.setModal(true);
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
