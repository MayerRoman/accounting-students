package dao;

import java.sql.SQLException;

/**
 * Created by Mayer Roman on 24.01.2016.
 */
public interface DAOFactory {
    GroupDAO getGroupDAO() throws SQLException;

    StudentDAO getStudentDAO() throws SQLException;

    void shutdownDataBase() throws SQLException;
}
