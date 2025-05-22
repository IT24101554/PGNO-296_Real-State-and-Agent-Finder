<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Book Appointment</title>
</head>
<body>
<h2>Book an Appointment</h2>
<form action="AppointmentServlet" method="post">
  Appointment ID: <input type="text" name="appointmentId" required><br><br>
  Client Name: <input type="text" name="clientName" required><br><br>
  Agent Name: <input type="text" name="agentName" required><br><br>
  Date: <input type="date" name="date" required><br><br>
  Time: <input type="time" name="time" required><br><br>
  <input type="submit" value="Book Appointment">
</form>
</body>
</html>
