package com.foxminded.university.model;

public class Group {
    int groupId;
    String groupName;

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public Group(int groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return groupId + " " + groupName;
    }

}
