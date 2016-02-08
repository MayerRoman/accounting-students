package dao.impl;

import dao.StudentDAO;
import dto.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mayer Roman on 21.01.2016.
 */
public class HSqlStudentDAO implements StudentDAO {
    private Connection connection;

    public HSqlStudentDAO(Connection connection) {
        this.connection = connection;
    }

    public long createStudent(Student student) throws SQLException {
        String sql = "INSERT INTO student " +
                "(studentId, studentName, studentSurname, studentPatronymic, studentDateOfBirth, studentGroupNumber) " +
                "VALUES (DEFAULT, ?, ?, ?, ?, ?);";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, student.getStudentName());
        statement.setString(2, student.getStudentSurname());
        statement.setString(3, student.getStudentPatronymic());
        statement.setDate(4, student.getStudentDateOfBirth());
        statement.setLong(5, student.getStudentGroupNumber());
        statement.executeUpdate();
        statement = connection.prepareStatement("CALL IDENTITY();");

        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        long studentId = resultSet.getLong(1);

        return studentId;
    }

    public Student readStudent(long studentId) throws SQLException {
        //изменить
        String sql = "SELECT * FROM student WHERE studentId=?;";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setLong(1, studentId);

        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        Student student = new Student();
        student.setStudentId(resultSet.getLong(1));
        student.setStudentName(resultSet.getString(2));
        student.setStudentSurname(resultSet.getString(3));
        student.setStudentPatronymic(resultSet.getString(4));
        student.setStudentDateOfBirth(resultSet.getDate(5));
        student.setStudentGroupNumber(resultSet.getInt(6));

        return student;
    }

    public void updateStudent(Student student) throws SQLException {
        String sql = "UPDATE student SET " +
                "(studentName, studentSurname, studentPatronymic, studentDateOfBirth, studentGroupNumber) =" +
                "(?, ?, ?, ?, ?) WHERE studentId=?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, student.getStudentName());
        statement.setString(2, student.getStudentSurname());
        statement.setString(3, student.getStudentPatronymic());
        statement.setDate(4, student.getStudentDateOfBirth());
        statement.setLong(5, student.getStudentGroupNumber());
        statement.setLong(6, student.getStudentId());

        statement.executeUpdate();
    }

    public void deleteStudent(long studentId) throws SQLException {
        String sql = "DELETE FROM student WHERE studentId=?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setLong(1, studentId);
        statement.executeUpdate();
    }

    public List<Student> getAllStudents() throws SQLException {
        String sql = "SELECT * FROM student;";

        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet resultSet = statement.executeQuery();
        List<Student> list = new ArrayList<Student>();
        Student student;
        while (resultSet.next()){
            student = new Student();
            student.setStudentId(resultSet.getLong(1));
            student.setStudentName(resultSet.getString(2));
            student.setStudentSurname(resultSet.getString(3));
            student.setStudentPatronymic(resultSet.getString(4));
            student.setStudentDateOfBirth(resultSet.getDate(5));
            student.setStudentGroupNumber(resultSet.getInt(6));
            list.add(student);
        }

        return list;
    }
}
