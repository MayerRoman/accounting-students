package dao.impl;

import dao.GroupDAO;
import dto.Group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mayer Roman on 21.01.2016.
 */
public class HSqlGroupDAO implements GroupDAO {
    private Connection connection;

    public HSqlGroupDAO(Connection connection) {
        this.connection = connection;
    }

    public long createGroup(Group group) throws SQLException {
        String sql = "INSERT INTO student_group (groupId, groupNumber, groupFaculty) VALUES (DEFAULT, ?, ?);";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, group.getGroupNumber());
        statement.setString(2, group.getGroupFaculty());
        statement.executeUpdate();
        statement = connection.prepareStatement("CALL IDENTITY();");

        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        long groupId = resultSet.getLong(1);

        return groupId;
    }

    public Group readGroup(long key) throws SQLException {
        String sql = "SELECT * FROM student_group WHERE GroupId=?;";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setLong(1, key);

        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        Group group = new Group();
        group.setGroupId(resultSet.getLong(1));
        group.setGroupNumber(resultSet.getInt(2));
        group.setGroupFaculty(resultSet.getString(3));

        return group;
    }

    public void updateGroup(Group group) throws SQLException {
        String sql = "UPDATE student_group SET (groupNumber, groupFaculty) = (?, ?)" +
                "WHERE groupId=?;";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, group.getGroupNumber());
        statement.setString(2, group.getGroupFaculty());
        statement.setLong(3, group.getGroupId());
        statement.executeUpdate();
    }

    public void deleteGroup(long groupId) throws SQLException {
        String sql = "DELETE FROM student_group WHERE groupId=?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setLong(1, groupId);
        statement.executeUpdate();
    }

    public List<Group> getAllGroups() throws SQLException {
        String sql = "SELECT * FROM student_group";

        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet resultSet = statement.executeQuery();
        List<Group> list = new ArrayList<Group>();
        Group group;
        while (resultSet.next()) {
            group = new Group();
            group.setGroupId(resultSet.getLong(1));
            group.setGroupNumber(resultSet.getInt(2));
            group.setGroupFaculty(resultSet.getString(3));
            list.add(group);
        }

        return list;
    }
}
