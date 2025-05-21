package com.dilshan.pgno296_realstateandagentfinder.servlet;

import com.dilshan.pgno296_realstateandagentfinder.model.Feedback;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@WebServlet("/deleteFeedback")
public class DeleteFeedbackServlet extends HttpServlet {

    private String getFilePath() {
        return getServletContext().getRealPath("/") + "data/Feedback.txt";
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String indexStr = request.getParameter("index");
        int indexToDelete;

        // Validate index parameter
        try {
            indexToDelete = Integer.parseInt(indexStr);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid index format.");
            return;
        }

        List<Feedback> feedbackList = FeedbackServlet.readFeedbackList(request);

        // Validate index bounds
        if (indexToDelete < 0 || indexToDelete >= feedbackList.size()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid index.");
            return;
        }

        // Remove the feedback entry
        feedbackList.remove(indexToDelete);

        // Write updated feedback list back to file
        String filePath = getFilePath();
        File file = new File(filePath);
        file.getParentFile().mkdirs();

        synchronized (this) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                for (Feedback f : feedbackList) {
                    writer.println(f.toFileString());
                }
            } catch (IOException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating feedback file.");
                return;
            }
        }

        response.sendRedirect("listFeedback.jsp?deleted=true");
    }
}