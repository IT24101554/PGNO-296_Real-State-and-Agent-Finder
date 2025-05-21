<%@ page import="java.util.List, com.dilshan.pgno296_realstateandagentfinder.model.Feedback, com.dilshan.pgno296_realstateandagentfinder.servlet.FeedbackServlet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Feedback List</title>
    <link rel="stylesheet" href="css/list.css">
</head>
<body>
<h2>Feedback List</h2>
<%
    List<Feedback> feedbackList = FeedbackServlet.readFeedbackList(request);
    if (feedbackList.isEmpty()) {
        System.out.println("<p>No feedback available.</p>");
    } else {
%>
<table border="1">
    <tr>
        <th>Name</th>
        <th>Email</th>
        <th>Rating</th>
        <th>Comments</th>
        <th>Action</th>
    </tr>
    <%
        for (int i = 0; i < feedbackList.size(); i++) {
            Feedback f = feedbackList.get(i);
    %>
    <tr>
        <td><%= f.getName() %></td>
        <td><%= f.getEmail() %></td>
        <td><%= f.getRating() %> ★</td>
        <td><%= f.getComments() %></td>
        <td>
            <form action="deleteFeedback" method="post">
                <input type="hidden" name="index" value="<%= i %>">
                <input type="submit" value="Delete">
            </form>
        </td>
    </tr>
    <% } %>
</table>
<% } %>
<a href="submitFeedback.jsp">Submit New Feedback</a>
</body>
</html>