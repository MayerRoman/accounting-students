package gui;

import dao.DAOFactory;
import dao.GroupDAO;
import dao.StudentDAO;
import dao.impl.HSqlDAOFactory;
import gui.dialog.exception.DataBaseExceptionDialog;
import gui.tab.GroupTab;
import gui.tab.StudentTab;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * Created by Mayer Roman on 22.01.2016.
 */
public class MainFrame extends JFrame implements Runnable {
    DAOFactory daoFactory;

    private void setFont() {
        FontUIResource f = new FontUIResource(new Font("HonMincho", 0, 15));
        Enumeration<Object> keys = UIManager.getDefaults().keys();

        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);

            if (value instanceof FontUIResource) {
                FontUIResource orig = (FontUIResource) value;
                Font font = new Font(f.getFontName(), orig.getStyle(), f.getSize());
                UIManager.put(key, new FontUIResource(font));
            }
        }
    }

    public void run() {
        GroupDAO groupDAO = null;
        StudentDAO studentDAO = null;

        try {
            daoFactory = new HSqlDAOFactory();
            groupDAO = daoFactory.getGroupDAO();
            studentDAO = daoFactory.getStudentDAO();
        } catch (SQLException e) {
            new DataBaseExceptionDialog(e);
        }

        setFont();

        this.setPreferredSize(new Dimension(1100, 600));
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false);
                shutdownDataBase();
                dispose();
            }
        });


        JTabbedPane tabbedPane = new JTabbedPane();
        this.add(tabbedPane);

        tabbedPane.add("Группы", new GroupTab(groupDAO));
        tabbedPane.add("Студенты", new StudentTab(groupDAO, studentDAO));


        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void shutdownDataBase() {
        try {
            daoFactory.shutdownDataBase();
        } catch (SQLException e) {
            new DataBaseExceptionDialog(e);
        }
    }

    public static void main(String[] args)throws SQLException {

        SwingUtilities.invokeLater(new MainFrame());
    }
}
