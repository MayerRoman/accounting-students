package dao;

import dto.Group;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Mayer Roman on 21.01.2016.
 */
public interface GroupDAO {

    long createGroup(Group group) throws SQLException;

    Group readGroup(long i) throws SQLException;

    void updateGroup(Group group)throws SQLException;

    void deleteGroup(long groupID) throws SQLException;

    List<Group> getAllGroups()throws SQLException;
}
