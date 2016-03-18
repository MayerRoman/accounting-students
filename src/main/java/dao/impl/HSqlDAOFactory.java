package dao.impl;

import dao.DAOFactory;
import dao.GroupDAO;
import dao.StudentDAO;
import gui.dialog.exception.DataBaseInitializingExceptionDialog;

import java.io.*;
import java.sql.*;

/**
 * Created by Mayer Roman on 21.01.2016.
 */
public class HSqlDAOFactory implements DAOFactory {
    private static final String DBURL = "jdbc:hsqldb:file:./db/testdb";
    private static final String user = "sa";
    private static final String password = "";
    private Connection connection;

    public HSqlDAOFactory()throws SQLException {
        boolean groupTableAlreadyExists = false;
        boolean studentTableAlreadyExists = false;
        boolean tablesInBaseAlreadyExists = false;

        connection = DriverManager.getConnection(DBURL, user, password);;
        DatabaseMetaData databaseMetaData = connection.getMetaData();

        String[] types = {"TABLE"};
        ResultSet groupTableQuery = databaseMetaData.getTables("PUBLIC", "PUBLIC", "STUDENT_GROUP", types);
        if (groupTableQuery.next()) {
            groupTableAlreadyExists = true;
        }

        ResultSet studentTableQuery = databaseMetaData.getTables("PUBLIC", "PUBLIC", "STUDENT", types);
        if (studentTableQuery.next()) {
            studentTableAlreadyExists = true;
        }

        if (groupTableAlreadyExists && studentTableAlreadyExists) {
            tablesInBaseAlreadyExists = true;
        }

        if (!tablesInBaseAlreadyExists) {
            try {
                InputStream inputStream=new FileInputStream("init.sql");
                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
                BufferedReader reader=new BufferedReader(inputStreamReader);

                StringBuilder sql = new StringBuilder();
                while (reader.ready()) {
                    sql.append(reader.readLine());
                }

                Statement statement = connection.createStatement();
                statement.executeUpdate(sql.toString());
            } catch (IOException e) {
                new DataBaseInitializingExceptionDialog(e);
            }
        }
    }

    public void shutdownDataBase() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("SHUTDOWN");
    }

    public GroupDAO getGroupDAO() throws SQLException {
        return new HSqlGroupDAO(connection);
    }

    public StudentDAO getStudentDAO() throws SQLException {
        return new HSqlStudentDAO(connection);
    }
}
