<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<nav class="navbar navbar-expand-lg navbar-light bg-light fixed-top">
    <div class="container">
        <a class="navbar-brand" href="index.jsp">Movie Ticket Booking</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ml-auto">
                <li class="nav-item">
                    <a class="nav-link" href="index.jsp">Home</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/showtime/list">Showtime</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="movies.jsp">Movies</a>
                </li>

                <c:choose>
                    <c:when test="${sessionScope.user != null}">
                        <li class="nav-item">
                            <a class="nav-link" href="userProfile.jsp">Profile</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/MyBookingsServlet">My Bookings</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="Userservlet?action=logout">Logout</a>
                        </li>
                        <li class="nav-item">
                            <span class="nav-link">Welcome, ${sessionScope.username}!</span>
                        </li>
                        <!-- Add admin-specific navigation options -->
                        <c:if test="${sessionScope.user.admin}">
                            <li class="nav-item">
                                <a class="nav-link" href="AdminServlet?action=showDashboard">
                                    <i class="fas fa-cog"></i> Admin Dashboard
                                </a>
                            </li>
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <a class="nav-link" href="login.jsp">Login</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="register.jsp">Register</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>