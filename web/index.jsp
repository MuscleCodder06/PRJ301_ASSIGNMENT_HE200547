<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
        <link rel="stylesheet" href="static/css/styles.css" />
        <style>
            body.login-page {
                margin: 0;
                min-height: 100vh;
                font-family: Arial, sans-serif;
                background: #f4f4f4;
                display: flex;
                align-items: center;
                justify-content: center;
                padding: 20px;
                box-sizing: border-box;
            }

            .login-box {
                width: 100%;
                max-width: 420px;
                background: #fff;
                border: 1px solid #ddd;
                border-radius: 10px;
                padding: 32px 28px;
                box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
            }

            .login-title {
                margin: 0 0 8px;
                text-align: center;
                font-size: 28px;
                color: #222;
            }

            .login-subtitle {
                margin: 0 0 24px;
                text-align: center;
                color: #666;
                font-size: 14px;
            }

            .login-form {
                background: transparent;
                border: none;
                padding: 0;
                margin: 0;
                width: 100%;
            }

            .login-field {
                margin-bottom: 16px;
            }

            .login-field label {
                display: block;
                margin-bottom: 6px;
                font-weight: bold;
                color: #333;
            }

            .login-input {
                width: 100%;
                padding: 10px 12px;
                border: 1px solid #ccc;
                border-radius: 6px;
                box-sizing: border-box;
                font-size: 14px;
            }

            .login-input:focus {
                outline: none;
                border-color: #cc0000;
            }

            .login-button {
                width: 100%;
                padding: 11px 16px;
                border: none;
                border-radius: 6px;
                background: #cc0000;
                color: #fff;
                font-weight: bold;
                cursor: pointer;
            }

            .login-button:hover {
                background: #a80000;
            }

            .login-error {
                margin-top: 14px;
                color: #cc0000;
                font-size: 14px;
                font-weight: bold;
                text-align: center;
            }

            .login-footer {
                margin-top: 18px;
                text-align: center;
            }

            .login-footer a {
                color: #cc0000;
                text-decoration: none;
                font-weight: bold;
                background: none;
                padding: 0;
                margin: 0;
            }

            .login-footer a:hover {
                text-decoration: underline;
            }
        </style>
    </head>
    <body class="login-page">
        <div class="login-box">
            <h2 class="login-title">Login</h2>
            <p class="login-subtitle">Sign in to manage repair orders and bookings.</p>

            <form action="Login" method="POST" class="login-form">
                <div class="login-field">
                    <label for="username">Username</label>
                    <input
                        id="username"
                        class="login-input"
                        type="text"
                        name="username"
                        value="${requestScope.username}"
                        placeholder="Enter username" />
                </div>

                <div class="login-field">
                    <label for="password">Password</label>
                    <input
                        id="password"
                        class="login-input"
                        type="password"
                        name="password"
                        value="${requestScope.password}"
                        placeholder="Enter password" />
                </div>

                <button type="submit" class="login-button">Login</button>

                <c:if test="${not empty requestScope.error}">
                    <div class="login-error">${requestScope.error}</div>
                </c:if>
            </form>

            <div class="login-footer">
                <a href="CustomerBooking">Customer booking form</a>
            </div>
        </div>
    </body>
</html>
