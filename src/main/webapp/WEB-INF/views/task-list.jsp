<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib uri="jakarta.tags.core" prefix="c" %> <%@
taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Task Management</title>
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
      rel="stylesheet"
    />
    <link
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"
      rel="stylesheet"
    />
    <style>
      .task-card {
        transition: transform 0.2s;
      }
      .task-card:hover {
        transform: translateY(-5px);
      }
      .priority-high {
        border-left: 4px solid #dc3545;
      }
      .priority-medium {
        border-left: 4px solid #ffc107;
      }
      .priority-low {
        border-left: 4px solid #28a745;
      }
    </style>
  </head>
  <body>
    <div class="container py-4">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h1 class="h3">Task Management</h1>
        <a
          href="${pageContext.request.contextPath}/tasks/new"
          class="btn btn-primary"
        >
          <i class="fas fa-plus"></i> New Task
        </a>
      </div>

      <div class="row">
        <c:forEach var="task" items="${tasks}">
          <div class="col-md-4 mb-4">
            <div
              class="card task-card priority-${task.priority == 1 ? 'high' : task.priority == 2 ? 'medium' : 'low'}"
            >
              <div class="card-body">
                <h5 class="card-title">${task.title}</h5>
                <p class="card-text">${task.description}</p>
                <div class="d-flex justify-content-between align-items-center">
                  <span
                    class="badge bg-${task.status == 'COMPLETED' ? 'success' : task.status == 'IN_PROGRESS' ? 'warning' : 'secondary'}"
                  >
                    ${task.status}
                  </span>
                  <small class="text-muted">
                    Due:
                    <fmt:formatDate
                      value="${task.dueDate}"
                      pattern="MMM dd, yyyy"
                    />
                  </small>
                </div>
                <div class="mt-3">
                  <a
                    href="${pageContext.request.contextPath}/tasks/edit?id=${task.id}"
                    class="btn btn-sm btn-outline-primary"
                  >
                    <i class="fas fa-edit"></i> Edit
                  </a>
                  <a
                    href="${pageContext.request.contextPath}/tasks/delete?id=${task.id}"
                    class="btn btn-sm btn-outline-danger"
                    onclick="return confirm('Are you sure you want to delete this task?')"
                  >
                    <i class="fas fa-trash"></i> Delete
                  </a>
                </div>
              </div>
            </div>
          </div>
        </c:forEach>
      </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
  </body>
</html>
