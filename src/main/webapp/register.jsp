<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function validateRegisterForm() {
            var email = document.getElementById("reg-email").value;
            var password = document.getElementById("reg-password").value;
            var emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
            if (!emailPattern.test(email)) {
                alert("Please enter a valid email address.");
                return false;
            }
            if (password.length < 6) {
                alert("Password must be at least 6 characters long.");
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
    <div class="container mt-5">
        <div class="form-container">
            <h2 class="text-center">Register</h2>
            <!-- Fix form attributes and field names to match servlet -->
            <form id="registerForm" action="Userservlet" method="post">
                <input type="hidden" name="action" value="register">
                
                <div class="form-group">
                    <label for="username">Username:</label>
                    <input type="text" class="form-control" id="username" name="username" required>
                    <div class="invalid-feedback"></div>
                </div>

                <div class="form-group">
                    <label for="name">Full Name:</label>
                    <input type="text" class="form-control" id="name" name="name" required>
                    <div class="invalid-feedback"></div>
                </div>

                <div class="form-group">
                    <label for="email">Email:</label>
                    <input type="email" class="form-control" id="email" name="email" required>
                    <div class="invalid-feedback"></div>
                </div>

                <div class="form-group">
                    <label for="country">Country:</label>
                    <input type="text" class="form-control" id="country" name="country" required>
                    <div class="invalid-feedback"></div>
                </div>

                <div class="form-group">
                    <label for="address">Address:</label>
                    <textarea class="form-control" id="address" name="address" required></textarea>
                    <div class="invalid-feedback"></div>
                </div>

                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" class="form-control" id="password" name="password" required>
                    <div class="invalid-feedback"></div>
                </div>

                <button type="submit" class="btn btn-primary btn-block">Register</button>
            </form>
            
            <!-- Add error message display -->
            <% if(request.getAttribute("error") != null) { %>
                <div class="alert alert-danger mt-3">
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>
        </div>
    </div>
    <!-- ...existing scripts... -->
</body>
</html>