<!-- New file: /webapp/userProfile.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>User Profile</title>
    <link href="css/styles.css" rel="stylesheet">
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <%@ include file="navbar.jsp" %>
    
    
    <div class="container mt-5">
         <div class="row"> 
            <!-- Profile Section -->
            <div class="col-md-6">
                <div class="profile-container">
                    <div class="profile-header">
                        <h2>User Profile</h2>
                        <p class="text-muted">Welcome, ${user.username}!</p>
                    </div>
                    
                    <!-- Add success/error message display -->
                    <% if(request.getParameter("updated") != null) { %>
                        <div class="alert alert-success">Profile updated successfully!</div>
                    <% } %>
                    <% if(request.getAttribute("error") != null) { %>
                        <div class="alert alert-danger">${error}</div>
                    <% } %>
                    
                    <form id="profileForm" action="${pageContext.request.contextPath}/Userservlet" method="post">
                        <input type="hidden" name="action" value="updateProfile">
                        <input type="hidden" name="user_id" value="${user.user_id}">
                        
                        <div class="form-group">
                            <label>Username:</label>
                            <input type="text" class="form-control" value="${user.username}" readonly>
                        </div>

                        <div class="form-group">
                            <label>Name:</label>
                            <input type="text" class="form-control" id="name" name="name" 
                                   value="${user.name}" data-validate="name" required>
                            <div id="nameFeedback" class="invalid-feedback"></div>
                        </div>

                        <div class="form-group">
                            <label>Email:</label>
                            <input type="email" class="form-control" id="email" name="email" 
                                   value="${user.email}" data-validate="email" required>
                            <div id="emailFeedback" class="invalid-feedback"></div>
                        </div>

                        <div class="form-group">
                            <label>Country:</label>
                            <input type="text" class="form-control" id="country" name="country" 
                                   value="${user.country}" required>
                            <div id="countryFeedback" class="invalid-feedback"></div>
                        </div>

                        <div class="form-group">
                            <label>Address:</label>
                            <textarea class="form-control" id="address" name="address" 
                                      data-validate="address" required>${user.address}</textarea>
                            <div id="addressFeedback" class="invalid-feedback"></div>
                        </div>

                        <button type="submit" class="btn btn-primary">Update Profile</button>
                    </form>
                </div>
            </div>
            
            <!-- Bookings Section -->
            
         </div> 
    </div>

    <!-- Add jQuery and form submission handler -->
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script>
        $(document).ready(function() {
            $('#profileForm').on('submit', function(e) {
                e.preventDefault();
                $.ajax({
                    type: 'POST',
                    url: $(this).attr('action'),
                    data: $(this).serialize(),
                    success: function(response) {
                        window.location.href = 'userProfile.jsp?updated=true';
                    },
                    error: function(xhr, status, error) {
                        alert('Error updating profile: ' + error);
                    }
                });
            });
        });
    </script>
    <script src="js/validation.js"></script>
</body>
</html>