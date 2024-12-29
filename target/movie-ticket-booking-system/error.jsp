<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Error</title>
    <link href="css/styles.css" rel="stylesheet">
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <%@ include file="navbar.jsp" %>
    
    <div class="container mt-5 pt-4">
        <div class="alert alert-danger">
            <h4>Error!</h4>
            <p>${errorMessage != null ? errorMessage : 'An unexpected error occurred.'}</p>
            <a href="index.jsp" class="btn btn-primary">Return to Home</a>
        </div>
    </div>
</body>
</html>
