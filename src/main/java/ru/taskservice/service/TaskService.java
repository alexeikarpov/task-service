package ru.taskservice.service;

import org.springframework.stereotype.Service;
import ru.taskservice.dto.*;
import ru.taskservice.enums.DefaultStatus;
import ru.taskservice.model.*;
import ru.taskservice.repository.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.*;

@Service
public class TaskService {

    public boolean addTask(Task task) {
        String sql = "INSERT INTO tasks (id, name, description, default_status, time_to_complete_seconds) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, task.getId());
            stmt.setString(2, task.getName());
            stmt.setString(3, task.getDescription());
            stmt.setString(4, task.getDefaultStatus().getStatus());
            stmt.setLong(5, task.getTimeToCompleteSeconds());
            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeTask(UUID id) {
        String sql = "DELETE FROM tasks WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Collection<Task> getAllTasks() {
        String sql = "SELECT id, name, description, default_status, time_to_complete_seconds FROM tasks";
        Collection<Task> tasks = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            taskDesign(tasks, rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }

    public Collection<Task> findTasks(String keyword) {
        String sql = "SELECT id, name, description, default_status, time_to_complete_seconds FROM tasks WHERE LOWER(name) LIKE ?";
        Collection<Task> tasks = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword.toLowerCase() + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                taskDesign(tasks, rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    private void taskDesign(Collection<Task> tasks, ResultSet rs) throws SQLException {
        while (rs.next()) {
            Task task = new Task();
            task.setId((UUID) rs.getObject("id"));
            task.setName(rs.getString("name"));
            task.setDescription(rs.getString("description"));
            task.setDefaultStatus(DefaultStatus.fromString(rs.getString("default_status")));
            task.setTimeToCompleteSeconds(rs.getLong("time_to_complete_seconds"));
            task.setTimeToComplete(Duration.ofSeconds(rs.getLong("time_to_complete_seconds")));
            tasks.add(task);
        }
    }

    public boolean updateTaskField(UUID taskId, UpdateTaskFieldRequest request) {
        String sql = "UPDATE tasks SET " + request.getFieldName() + " = ? WHERE id = ?";

        // Список разрешённых для обновления полей
        Set<String> allowedFields = Set.of("name", "description", "default_status");

        // Проверяем, что переданное поле допустимо
        if (!allowedFields.contains(request.getFieldName())) {
            throw new IllegalArgumentException("Недопустимое поле для обновления: " + request.getFieldName());
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, request.getFieldValue()); // Устанавливаем новое значение для поля
            stmt.setObject(2, taskId);   // Устанавливаем идентификатор задачи

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Возвращаем true, если обновление прошло успешно

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean updateTimeToComplete(UUID id, UpdateTimeRequest request) {
        String selectSql = "SELECT time_to_complete_seconds FROM tasks WHERE id = ?";
        String updateSql = "UPDATE tasks SET time_to_complete_seconds = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            long currentTimeToComplete;

            try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                selectStmt.setObject(1, id);

                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        currentTimeToComplete = rs.getLong("time_to_complete_seconds");
                    } else {
                        return false;
                    }
                }
            }

            if (request.getDuration().getSeconds() > currentTimeToComplete)
                return false;

            long updatedTimeToComplete = currentTimeToComplete - request.getDuration().getSeconds();

            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setLong(1, updatedTimeToComplete);
                updateStmt.setObject(2, id);
                int rowsAffected = updateStmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
