package dto;

/**
 * Created by Mayer Roman on 21.01.2016.
 */
public class Group {
    private long groupId;
    private int groupNumber;
    private String groupFaculty;

    public Group(){};
    public Group(int groupNumber, String faculty) {
        this.groupNumber = groupNumber;
        this.groupFaculty = faculty;
    }


    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }


    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }


    public String getGroupFaculty() {
        return groupFaculty;
    }

    public void setGroupFaculty(String groupFaculty) {
        this.groupFaculty = groupFaculty;
    }
}
