<%@ page import="model.Feedback, servlet.FeedbackServlet" %>
<%@ page import="java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Feedback Submissions</title>
    <link rel="stylesheet" href="css/list.css">
    <script src="js/list.js" defer></script>
</head>
<body>

<nav>
    <a href="submitFeedback.jsp">Submit Feedback</a>
    <a href="listFeedback.jsp">View Feedback</a>
    <a href="index.jsp">Home</a>
</nav>

<div class="container">
    <h2>Feedback Submissions</h2>

    <!-- Enhanced Success Message -->
    <%
        String deleted = request.getParameter("deleted");
        if ("true".equals(deleted)) {
    %>
    <div id="successBox" class="success-notification">
        <div class="success-icon">✓</div>
        <div class="success-content">
            <h4>Success!</h4>
            <p>The feedback has been successfully deleted from the system.</p>
        </div>
        <button class="close-btn" onclick="closeNotification()">×</button>
    </div>
    <%
        }
    %>

    <input type="text" id="searchInput" placeholder="Search by name..." onkeyup="filterFeedbacks()">

    <%
        List<Feedback> feedbackList = FeedbackServlet.readFeedbackList();
        Map<String, List<Integer>> instructorRatings = new HashMap<>();
        for (Feedback fb : feedbackList) {
            instructorRatings.computeIfAbsent(fb.getInstructor(), k -> new ArrayList<>()).add(fb.getRating());
        }
    %>

    <div class="averages">
        <h3>📊 Average Ratings by Instructor</h3>
        <ul>
            <%
                for (Map.Entry<String, List<Integer>> entry : instructorRatings.entrySet()) {
                    String instructor = entry.getKey();
                    List<Integer> ratings = entry.getValue();
                    double avg = ratings.stream().mapToInt(r -> r).average().orElse(0);
            %>
            <li><strong><%= instructor %>:</strong>
                <% for (int s = 1; s <= 5; s++) { %>
                <% if (s <= Math.round(avg)) { %>
                <span class="star filled">★</span>
                <% } else { %>
                <span class="star empty">☆</span>
                <% } %>
                <% } %>
                (Avg: <%= String.format("%.2f", avg) %>)
            </li>
            <% } %>
        </ul>
    </div>

    <table>
        <thead>
        <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Instructor</th>
            <th>Rating</th>
            <th>Comments</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (int i = 0; i < feedbackList.size(); i++) {
                Feedback f = feedbackList.get(i);
        %>
        <tr>
            <td><%= f.getName() %></td>
            <td><%= f.getEmail() %></td>
            <td><%= f.getInstructor() %></td>
            <td>
                <% for (int r = 0; r < f.getRating(); r++) { %>
                <span class="star filled">★</span>
                <% } %>
                <% for (int r = f.getRating(); r < 5; r++) { %>
                <span class="star empty">☆</span>
                <% } %>
            </td>
            <td><%= f.getComments() %></td>
            <td>
                <form id="deleteForm<%= i %>" method="post" action="deleteFeedback">
                    <input type="hidden" name="index" value="<%= i %>">
                    <button type="button" onclick="confirmDelete(<%= i %>, '<%= f.getName() %>')">🗑 Delete</button>
                </form>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>

    <div class="back-button">
        <a href="index.jsp">← Back to Home Page</a>
    </div>
</div>

</body>
</html>
