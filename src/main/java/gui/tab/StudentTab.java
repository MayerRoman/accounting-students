package gui.tab;

import dao.GroupDAO;
import dao.StudentDAO;
import dto.Student;
import gui.dialog.exception.DataBaseExceptionDialog;
import gui.dialog.student.StudentAddDialog;
import gui.dialog.student.StudentChangeDialog;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Mayer Roman on 17.01.2016.
 */
public class StudentTab extends JPanel {
    private JTextField filterOfSurnamesInTable;
    private JTextField filterOfGroupsInTable;
    private TableRowSorter<StudentTableModel> tableSorter;
    private StudentDAO studentDAO;
    private GroupDAO groupDAO;
    public static List<Student> students;
    public static JTable studentTable;
    public static StudentTableModel studentTableModel;


    public StudentTab(GroupDAO groupDAO, StudentDAO studentDAO) {
        super(new BorderLayout());

        this.groupDAO = groupDAO;
        this.studentDAO = studentDAO;

        this.add(createScrollPaneWithTable(),BorderLayout.CENTER);
        this.add(createPanelWithFiltersAndButtons(),BorderLayout.SOUTH);
    }


    private JScrollPane createScrollPaneWithTable() {
        studentTable = new JTable();

        studentTableModel = new StudentTableModel();
        studentTable.setModel(studentTableModel);

        tableSorter = new TableRowSorter<StudentTableModel>(studentTableModel);
        studentTable.setRowSorter(tableSorter);

        studentTable.setFillsViewportHeight(true);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        JScrollPane scrollPane = new JScrollPane(studentTable);

        return scrollPane;
    }

    public class StudentTableModel extends AbstractTableModel {
        String[] columnNames = {"Имя", "Фамилия", "Отчество", "Дата рождения", "Группа"};

        List<Student> studentsFromDataBase = getStudentsFromDataBase();

        public int getRowCount() {
            return studentsFromDataBase.size();
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex){
                case 0:
                    return studentsFromDataBase.get(rowIndex).getStudentName();
                case 1:
                    return studentsFromDataBase.get(rowIndex).getStudentSurname();
                case 2:
                    return studentsFromDataBase.get(rowIndex).getStudentPatronymic();
                case 3:
                    return studentsFromDataBase.get(rowIndex).getStudentDateOfBirth();
                case 4:
                    return studentsFromDataBase.get(rowIndex).getStudentGroupNumber();
                default:
                    return "";
            }
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }

    private List<Student> getStudentsFromDataBase() {
        try {
            students = studentDAO.getAllStudents();
        } catch (SQLException e) {
            new DataBaseExceptionDialog(e);
        }
        return students;
    }


    private JPanel createPanelWithFiltersAndButtons() {
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(createPanelWithFilters(), BorderLayout.CENTER);
        panel.add(createPanelWithButtons(), BorderLayout.SOUTH);

        return panel;
    }


    private JPanel createPanelWithFilters() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setPreferredSize(new Dimension(900,100));


        JPanel surnameFilterPanel = new JPanel();
        panel.add(surnameFilterPanel);

        JLabel surnameFilterLabel = new JLabel("Фильтровать по фамилии:");
        surnameFilterPanel.add(surnameFilterLabel);

        filterOfSurnamesInTable = new JTextField(20);
        filterOfSurnamesInTable.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateFilterOfSurnamesInTable();
            }

            public void removeUpdate(DocumentEvent e) {
                updateFilterOfSurnamesInTable();
            }

            public void changedUpdate(DocumentEvent e) {
                updateFilterOfSurnamesInTable();
            }
        });
        surnameFilterPanel.add(filterOfSurnamesInTable);

        SpringLayout springLayout1 = new SpringLayout();
        springLayout1.putConstraint(SpringLayout.EAST, surnameFilterLabel, -15, SpringLayout.WEST, filterOfSurnamesInTable);
        springLayout1.putConstraint(SpringLayout.NORTH, surnameFilterLabel, 15, SpringLayout.NORTH, surnameFilterPanel);
        springLayout1.putConstraint(SpringLayout.WEST, filterOfSurnamesInTable, 255, SpringLayout.WEST, surnameFilterPanel);
        springLayout1.putConstraint(SpringLayout.NORTH, filterOfSurnamesInTable, 15, SpringLayout.NORTH, surnameFilterPanel);
        surnameFilterPanel.setLayout(springLayout1);


        JPanel groupsFilterPanel = new JPanel();
        panel.add(groupsFilterPanel);

        JLabel groupsFilterLabel = new JLabel("Фильтровать по группе:");
        groupsFilterPanel.add(groupsFilterLabel);

        filterOfGroupsInTable = new JTextField(20);
        filterOfGroupsInTable.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateFilterOfGroupsInTable();
            }

            public void removeUpdate(DocumentEvent e) {
                updateFilterOfGroupsInTable();
            }

            public void changedUpdate(DocumentEvent e) {
                updateFilterOfGroupsInTable();
            }
        });
        groupsFilterPanel.add(filterOfGroupsInTable);

        SpringLayout springLayout2 = new SpringLayout();
        springLayout2.putConstraint(SpringLayout.EAST, groupsFilterLabel, -15, SpringLayout.WEST, filterOfGroupsInTable);
        springLayout2.putConstraint(SpringLayout.NORTH, groupsFilterLabel, 5, SpringLayout.NORTH, groupsFilterPanel);
        springLayout2.putConstraint(SpringLayout.WEST, filterOfGroupsInTable, 255, SpringLayout.WEST, groupsFilterPanel);
        springLayout2.putConstraint(SpringLayout.NORTH, filterOfGroupsInTable, 5, SpringLayout.NORTH, groupsFilterPanel);
        groupsFilterPanel.setLayout(springLayout2);


        return panel;
    }

    private void updateFilterOfSurnamesInTable() {
        RowFilter<StudentTableModel, Object> rowFilter;

        try {
            rowFilter = RowFilter.regexFilter("^(?iu)" + filterOfSurnamesInTable.getText(), 1);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }

        tableSorter.setRowFilter(rowFilter);
    }

    private void updateFilterOfGroupsInTable() {
        RowFilter<StudentTableModel, Object> rowFilter;

        try {
            rowFilter = RowFilter.regexFilter("^"+ filterOfGroupsInTable.getText(), 4);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }

        tableSorter.setRowFilter(rowFilter);
    }


    private JPanel createPanelWithButtons() {
        JPanel panel = new JPanel(new GridLayout(1, 3));

        JButton addButton = new JButton("Добавить");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new StudentAddDialog(groupDAO, studentDAO);
            }
        });
        panel.add(addButton);

        JButton changeButton = new JButton("Изменить");
        changeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int numberOfSelectedRow = studentTable.getSelectedRow();

                if (numberOfSelectedRow >= 0) {
                    new StudentChangeDialog(groupDAO, studentDAO);
                }

            }
        });
        panel.add(changeButton);

        JButton deleteButton = new JButton("Удалить");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int numberOfSelectedRow = studentTable.getSelectedRow();

                if (numberOfSelectedRow >= 0) {
                    int indexOfSelectedStudent = studentTable.convertRowIndexToModel(numberOfSelectedRow);

                    try {
                        studentDAO.deleteStudent(students.get(indexOfSelectedStudent).getStudentId());
                        students.remove(indexOfSelectedStudent);
                        studentTableModel.fireTableDataChanged();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }

            }
        });
        panel.add(deleteButton);

        return panel;
    }
}
