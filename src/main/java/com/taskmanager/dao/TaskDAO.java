package com.taskmanager.dao;

import com.taskmanager.model.Task;
import com.taskmanager.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskDAO {
    
    public void createTask(Task task) throws SQLException {
        String sql = "INSERT INTO tasks (title, description, due_date, status, priority, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
                    
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setTimestamp(3, new Timestamp(task.getDueDate().getTime()));
            pstmt.setString(4, task.getStatus());
            pstmt.setInt(5, task.getPriority());
            pstmt.setTimestamp(6, new Timestamp(task.getCreatedAt().getTime()));
            pstmt.setTimestamp(7, new Timestamp(task.getUpdatedAt().getTime()));
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    task.setId(generatedKeys.getInt(1));
                }
            }
        }
    }
    
    public List<Task> getAllTasks() throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks ORDER BY due_date ASC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setDueDate(rs.getTimestamp("due_date"));
                task.setStatus(rs.getString("status"));
                task.setPriority(rs.getInt("priority"));
                task.setCreatedAt(rs.getTimestamp("created_at"));
                task.setUpdatedAt(rs.getTimestamp("updated_at"));
                tasks.add(task);
            }
        }
        return tasks;
    }
    
    public Task getTaskById(int id) throws SQLException {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Task task = new Task();
                    task.setId(rs.getInt("id"));
                    task.setTitle(rs.getString("title"));
                    task.setDescription(rs.getString("description"));
                    task.setDueDate(rs.getTimestamp("due_date"));
                    task.setStatus(rs.getString("status"));
                    task.setPriority(rs.getInt("priority"));
                    task.setCreatedAt(rs.getTimestamp("created_at"));
                    task.setUpdatedAt(rs.getTimestamp("updated_at"));
                    return task;
                }
            }
        }
        return null;
    }
    
    public void updateTask(Task task) throws SQLException {
        String sql = "UPDATE tasks SET title = ?, description = ?, due_date = ?, " +
                    "status = ?, priority = ?, updated_at = ? WHERE id = ?";
                    
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setTimestamp(3, new Timestamp(task.getDueDate().getTime()));
            pstmt.setString(4, task.getStatus());
            pstmt.setInt(5, task.getPriority());
            pstmt.setTimestamp(6, new Timestamp(new Date().getTime()));
            pstmt.setInt(7, task.getId());
            
            pstmt.executeUpdate();
        }
    }
    
    public void deleteTask(int id) throws SQLException {
        String sql = "DELETE FROM tasks WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
} 