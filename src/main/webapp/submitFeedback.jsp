<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Submit Feedback</title>
    <link rel="stylesheet" href="css/submit.css">
</head>
<body>

<nav>
    <a href="submitFeedback.jsp">Submit Feedback</a>
    <a href="listFeedback.jsp">View Feedback</a>
    <a href="index.jsp">Home</a>
</nav>

<div class="container">
    <h2>Submit Your Feedback</h2>
    <form action="submitFeedback" method="post">
        <label for="name">Full Name</label>
        <input type="text" id="name" name="name" required>

        <label for="email">Email</label>
        <input type="email" id="email" name="email" required>



        <label for="rating">Rating</label>
        <select id="rating" name="rating" required>
            <option value="" disabled selected>Select Rating</option>
            <option value="5">★★★★★</option>
            <option value="4">★★★★☆</option>
            <option value="3">★★★☆☆</option>
            <option value="2">★★☆☆☆</option>
            <option value="1">★☆☆☆☆</option>
        </select>

        <label for="comments">Comments</label>
        <textarea id="comments" name="comments" rows="4" required></textarea>

        <div class="buttons">
            <button type="submit">✅ Submit</button>
            <a href="listFeedback.jsp"><button type="button">📄 View Feedback</button></a>
        </div>
    </form>
</div>

</body>
</html>
