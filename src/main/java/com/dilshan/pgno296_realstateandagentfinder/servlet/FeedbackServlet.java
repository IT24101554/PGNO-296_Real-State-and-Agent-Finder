package com.dilshan.pgno296_realstateandagentfinder.servlet;

import com.dilshan.pgno296_realstateandagentfinder.model.Feedback;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

@WebServlet("/submitFeedback")
public class FeedbackServlet extends HttpServlet {

    String relativePath = "data/Feedback.txt";
    String absolutePath = getServletContext().getRealPath("/") + relativePath;

    private String getFilePath() {
        return getServletContext().getRealPath("/") + "data/Feedback.txt";
    }
    String path = getFilePath();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        int rating = Integer.parseInt(request.getParameter("rating"));
        String comments = request.getParameter("comments");

        Feedback feedback = new Feedback(name, email, rating, comments);

        String filePath = getServletContext().getRealPath("/") + "data/Feedback.txt";

        File file = new File(filePath);
        file.getParentFile().mkdirs();

        try (PrintWriter out = new PrintWriter(new FileWriter(getFilePath(), true))) {
            out.println(feedback.toFileString());
        }

        response.sendRedirect("listFeedback.jsp");
    }

    public static List<Feedback> readFeedbackList(HttpServletRequest request) throws IOException {
        List<Feedback> list = new ArrayList<>();

        String filepath = request.getServletContext().getRealPath("/") + "data/Feedback.txt";
        Path path = Paths.get(filepath);

        if (Files.exists(path)) {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                Feedback f = Feedback.fromFileString(line);
                if (f != null) {
                    list.add(f);
                }
            }
        }

        return list;
    }
}

