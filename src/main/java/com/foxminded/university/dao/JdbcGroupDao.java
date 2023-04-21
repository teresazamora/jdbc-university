package com.foxminded.university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.foxminded.university.DataBaseConnection;
import com.foxminded.university.model.Group;

public class JdbcGroupDao implements GroupDao {
    private DataBaseConnection dataConnection;

    public JdbcGroupDao(DataBaseConnection dataConnection) {
        this.dataConnection = dataConnection;
    }

    String ADD_NEW_GROUP = "INSERT INTO groups (group_name) VALUES(?)";
    String DELETE_BY_ID = "DELETE FROM groups WHERE group_id = ?";
    String FIND_BY_ALMOUNT_OF_STUDENTS = "SELECT groups.group_id, groups.group_name FROM groups INNER JOIN students ON groups.group_id = students.group_id GROUP BY groups.group_id, groups.group_name HAVING COUNT(*) <= ?";

    @Override
    public void create(Group group) {
        try (Connection connection = dataConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(ADD_NEW_GROUP,
                        Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, group.getGroupName());
            preparedStatement.execute();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                generatedKeys.next();
                group.setGroupId(generatedKeys.getInt(1));
            }
        } catch (Exception e) {
            e.getStackTrace();
        }

    }

    @Override
    public void delete(int groupId) {
        try (Connection connection = dataConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID)) {
            preparedStatement.setInt(1, groupId);
            preparedStatement.execute();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    @Override
    public List<Group> findGroupByAmountStudent(int amount) {
        List<Group> groups = new ArrayList<>();
        try (Connection connection = dataConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ALMOUNT_OF_STUDENTS)) {
            preparedStatement.setInt(1, amount);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                groups.add(new Group(resultSet.getInt("group_id"), resultSet.getString("group_name")));
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return groups;
    }

}
