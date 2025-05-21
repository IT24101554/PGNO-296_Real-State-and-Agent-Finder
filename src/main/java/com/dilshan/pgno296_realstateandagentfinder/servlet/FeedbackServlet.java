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

    private String getFilePath() {
        return getServletContext().getRealPath("/") + "data/Feedback.txt";
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        int rating = Integer.parseInt(request.getParameter("rating"));
        String comments = request.getParameter("comments");

        if(name==null || email==null || rating==0 || comments==null || name.trim().isEmpty() || email.trim().isEmpty()){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Please fill all the required fields");
            return;
        }

        Feedback feedback = new Feedback(name, email, rating, comments);

        String filePath = getFilePath();
        File file = new File(filePath);
        file.getParentFile().mkdirs();

        synchronized (this) {
            try (PrintWriter out = new PrintWriter(new FileWriter(file, true))) {
                out.println(feedback.toFileString());
            } catch (IOException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error saving feedback.");
                return;
            }
        }

        response.sendRedirect("listFeedback.jsp");
    }

    public static List<Feedback> readFeedbackList(HttpServletRequest request) throws IOException {
        List<Feedback> list = new ArrayList<>();

        String filepath = request.getServletContext().getRealPath("/") + "data/Feedback.txt";
        Path path = Paths.get(filepath);

        if (Files.exists(path)) {
            try{
                List<String> lines = Files.readAllLines(path);
                for (String line : lines) {
                    Feedback f = Feedback.fromFileString(line);
                    if (f != null) {
                        list.add(f);
                    }
                }
            }catch (IOException e){
                throw new IOException("Error reading feedback list" + e.getMessage());
            }
        }

        return list;
    }
}

