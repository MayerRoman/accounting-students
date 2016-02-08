package gui.tab;

import dao.GroupDAO;
import dto.Group;
import dto.Student;
import gui.dialog.exception.DataBaseExceptionDialog;
import gui.dialog.group.GroupAddDialog;
import gui.dialog.group.GroupChangeDialog;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Mayer Roman on 17.01.2016.
 */
public class GroupTab extends JPanel {
    public static List<Group> groups;
    public static GroupTableModel groupTableModel;
    public static JTable groupTable;
    private GroupDAO groupDAO;

    public GroupTab(GroupDAO groupDAO) {
        super(new BorderLayout());

        this.groupDAO = groupDAO;

        this.add(createScrollPaneWithTable(), BorderLayout.CENTER);
        this.add(createPanelWithButtons(), BorderLayout.SOUTH);

    }


    private JScrollPane createScrollPaneWithTable() {
        groupTable = new JTable(groupTableModel);

        groupTableModel = new GroupTableModel();
        groupTable.setModel(groupTableModel);

        groupTable.setAutoCreateRowSorter(true);
        groupTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        groupTable.setFillsViewportHeight(true);


        JScrollPane scrollPane = new JScrollPane(groupTable);

        return scrollPane;
    }

    public class GroupTableModel extends AbstractTableModel {
        String[] columnNames = {"Номер", "Название факультета"};

        List<Group> groupsFromDataBase = getGroupsFromDataBase();

        public int getRowCount() {
            return groupsFromDataBase.size();
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return groupsFromDataBase.get(rowIndex).getGroupNumber();
                case 1:
                    return groupsFromDataBase.get(rowIndex).getGroupFaculty();
                default:
                    return "";
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }
    }

    private List<Group> getGroupsFromDataBase() {
        try {
            groups = groupDAO.getAllGroups();
        } catch (SQLException e) {
            new DataBaseExceptionDialog(e);
        }
        return groups;
    }


    private JPanel createPanelWithButtons() {
        JPanel panel = new JPanel(new GridLayout(1, 3));

        JButton addButton = new JButton("Добавить");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new GroupAddDialog(groupDAO);
            }
        });
        panel.add(addButton);

        JButton changeButton = new JButton("Изменить");
        changeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int numberOfSelectedRow = groupTable.getSelectedRow();
                if (numberOfSelectedRow >= 0) {
                    new GroupChangeDialog(groupDAO);
                }
            }
        });
        panel.add(changeButton);

        final JButton deleteButton = new JButton("Удалить");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int numberOfSelectedRow = groupTable.getSelectedRow();

                if (numberOfSelectedRow >= 0) {
                    int indexOfSelectedGroup = groupTable.convertRowIndexToModel(numberOfSelectedRow);
                    Group group = groups.get(indexOfSelectedGroup);

                    boolean studentInGroup = false;
                    for (Student student : StudentTab.students) {
                        if (group.getGroupNumber() == student.getStudentGroupNumber()) {
                            studentInGroup = true;
                            break;
                        }
                    }

                    if (!studentInGroup) {
                        try {
                            groupDAO.deleteGroup(groups.get(indexOfSelectedGroup).getGroupId());
                            groups.remove(indexOfSelectedGroup);
                            groupTableModel.fireTableDataChanged();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }

                    else {
                        final JDialog dialog = new JDialog();

                        JLabel label = new JLabel("Нельзя удалить группу, в которой есть студенты", SwingConstants.CENTER);
                        dialog.add(label, BorderLayout.CENTER);

                        JButton button = new JButton("ОК");
                        button.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                dialog.dispose();
                            }
                        });
                        dialog.add(button, BorderLayout.SOUTH);

                        dialog.setModal(true);
                        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                        dialog.setPreferredSize(new Dimension(460, 200));
                        dialog.pack();
                        dialog.setLocationRelativeTo(null);
                        dialog.setVisible(true);
                    }
                }
            }
        });
        panel.add(deleteButton);

        return panel;
    }
}
