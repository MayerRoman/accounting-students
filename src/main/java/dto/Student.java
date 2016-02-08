package dto;

import java.sql.Date;

/**
 * Created by Mayer Roman on 21.01.2016.
 */
public class Student {
    private long studentId;
    private String studentName;
    private String studentSurname;
    private String studentPatronymic;
    private Date studentDateOfBirth;
    private int studentGroupNumber;

    public Student(){};
    public Student(String studentName, String studentSurname, String studentPatronymic, Date studentDateOfBirth, int studentGroupID) {
        this.studentName = studentName;
        this.studentSurname = studentSurname;
        this.studentPatronymic = studentPatronymic;
        this.studentDateOfBirth = studentDateOfBirth;
        this.studentGroupNumber = studentGroupID;
    }


    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }


    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }


    public String getStudentSurname() {
        return studentSurname;
    }

    public void setStudentSurname(String studentSurname) {
        this.studentSurname = studentSurname;
    }


    public String getStudentPatronymic() {
        return studentPatronymic;
    }

    public void setStudentPatronymic(String studentPatronymic) {
        this.studentPatronymic = studentPatronymic;
    }


    public Date getStudentDateOfBirth() {
        return studentDateOfBirth;
    }

    public void setStudentDateOfBirth(Date studentDateOfBirth) {
        this.studentDateOfBirth = studentDateOfBirth;
    }


    public int getStudentGroupNumber() {
        return studentGroupNumber;
    }

    public void setStudentGroupNumber(int studentGroupNumber) {
        this.studentGroupNumber = studentGroupNumber;
    }
}
