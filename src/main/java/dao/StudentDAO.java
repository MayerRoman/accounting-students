package dao;

import dto.Student;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Mayer Roman on 21.01.2016.
 */
public interface StudentDAO {

    long createStudent(Student student) throws SQLException;

    Student readStudent(long key) throws SQLException;

    void updateStudent(Student student) throws SQLException;

    void deleteStudent(long studentId) throws SQLException;

    List<Student> getAllStudents() throws SQLException;

}
