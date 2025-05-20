package com.dilshan.pgno296_realstateandagentfinder.servlet;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.servlet.annotation.WebServlet;
import com.dilshan.pgno296_realstateandagentfinder.model.Feedback;

@WebServlet({"/deleteFeedback"})
public class DeleteFeedbackServlet extends HttpServlet {

    String relativePath = "data/Feedback.txt";
    String absolutePath = getServletContext().getRealPath("/") + relativePath;

    private String getFilePath() {
        return getServletContext().getRealPath("/") + "data/Feedback.txt";
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int indexToDelete = Integer.parseInt(request.getParameter("index"));
        List<Feedback> feedbackList = FeedbackServlet.readFeedbackList(request);
        if (indexToDelete >= 0 && indexToDelete < feedbackList.size()) {
            feedbackList.remove(indexToDelete);
        }

        PrintWriter writer = new PrintWriter(new FileWriter(getFilePath()));

        try {
            for(Feedback f : feedbackList) {
                writer.println(f.toFileString());
            }
        } catch (Throwable var9) {
            try {
                writer.close();
            } catch (Throwable var8) {
                var9.addSuppressed(var8);
            }

            throw var9;
        }

        writer.close();
        response.sendRedirect("listFeedback.jsp?deleted=true");
    }
}
