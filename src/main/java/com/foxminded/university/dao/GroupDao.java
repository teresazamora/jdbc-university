package com.foxminded.university.dao;

import java.util.List;

import com.foxminded.university.model.Group;

public interface GroupDao {
    void create(Group group);
    void delete(int groupId);
    List<Group> findByStudentsAmount(int amount);

}
