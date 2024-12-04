<!-- New file: /webapp/userProfile.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>User Profile</title>
    <link href="css/styles.css" rel="stylesheet">
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <div class="profile-container">
            <h2>User Profile</h2>
            <form id="profileForm" onsubmit="return validateProfileForm()">
                <div class="form-group">
                    <label>Name:</label>
                    <input type="text" class="form-control" id="name" value="${user.name}" required>
                </div>
                <div class="form-group">
                    <label>Email:</label>
                    <input type="email" class="form-control" id="email" value="${user.email}" required>
                </div>
                <div class="form-group">
                    <label>Country:</label>
                    <input type="text" class="form-control" id="country" value="${user.country}" required>
                    
                </div>
                <button type="submit" class="btn btn-primary">Update Profile</button>
            </form>
        </div>
    </div>
</body>
</html>