<%@ page import="java.util.*, Appointment" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Admin - Appointments</title>
</head>
<body>
<h2>All Appointments</h2>
<table border="1">
  <tr>
    <th>ID</th>
    <th>Client Name</th>
    <th>Agent Name</th>
    <th>Date</th>
    <th>Time</th>
  </tr>
  <%
    List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
    if (appointments != null) {
      for (Appointment a : appointments) {
  %>
  <tr>
    <td><%= a.getAppointmentId() %></td>
    <td><%= a.getClientName() %></td>
    <td><%= a.getAgentName() %></td>
    <td><%= a.getDate() %></td>
    <td><%= a.getTime() %></td>
  </tr>
  <%  }
  }
  %>
</table>
</body>
</html>
