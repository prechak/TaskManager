package com.taskmanager.controller;

import com.taskmanager.dao.TaskDAO;
import com.taskmanager.model.Task;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet("/tasks/*")
public class TaskServlet extends HttpServlet {
    private TaskDAO taskDAO;
    private SimpleDateFormat dateFormat;

    @Override
    public void init() throws ServletException {
        taskDAO = new TaskDAO();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        
        try {
            if (action == null || action.equals("/")) {
                listTasks(request, response);
            } else if (action.equals("/new")) {
                showNewForm(request, response);
            } else if (action.equals("/edit")) {
                showEditForm(request, response);
            } else if (action.equals("/delete")) {
                deleteTask(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        
        try {
            if (action == null || action.equals("/")) {
                insertTask(request, response);
            } else if (action.equals("/update")) {
                updateTask(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException | ParseException e) {
            throw new ServletException(e);
        }
    }

    private void listTasks(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Task> tasks = taskDAO.getAllTasks();
        request.setAttribute("tasks", tasks);
        request.getRequestDispatcher("/WEB-INF/views/task-list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/task-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Task task = taskDAO.getTaskById(id);
        request.setAttribute("task", task);
        request.getRequestDispatcher("/WEB-INF/views/task-form.jsp").forward(request, response);
    }

    private void insertTask(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ParseException {
        Task task = new Task();
        task.setTitle(request.getParameter("title"));
        task.setDescription(request.getParameter("description"));
        task.setDueDate(dateFormat.parse(request.getParameter("dueDate")));
        task.setStatus(request.getParameter("status"));
        task.setPriority(Integer.parseInt(request.getParameter("priority")));
        
        taskDAO.createTask(task);
        response.sendRedirect(request.getContextPath() + "/tasks");
    }

    private void updateTask(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ParseException {
        Task task = new Task();
        task.setId(Integer.parseInt(request.getParameter("id")));
        task.setTitle(request.getParameter("title"));
        task.setDescription(request.getParameter("description"));
        task.setDueDate(dateFormat.parse(request.getParameter("dueDate")));
        task.setStatus(request.getParameter("status"));
        task.setPriority(Integer.parseInt(request.getParameter("priority")));
        
        taskDAO.updateTask(task);
        response.sendRedirect(request.getContextPath() + "/tasks");
    }

    private void deleteTask(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        taskDAO.deleteTask(id);
        response.sendRedirect(request.getContextPath() + "/tasks");
    }
} 