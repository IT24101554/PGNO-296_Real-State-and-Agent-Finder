import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class AppointmentServlet extends httpServlet {
    private AppointmentService manager = new AppointmentService();

    protected void doPost(httpServletRequest request, httpServletResponse response) throws servletException, IOException {
        String id = request.getParameter("appointmentId");
        String client = request.getParameter("clientName");
        String agent = request.getParameter("agentName");
        String date = request.getParameter("date");
        String time = request.getParameter("time");

        Appointment a = new Appointment(id, client, agent, date, time);
        manager.addAppointment(a);

        response.sendRedirect("admin-appointments.jsp");
    }

    protected void doGet(httpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Appointment> appointments = manager.getAllAppointments();
        request.setAttribute("appointments", appointments);
        RequestDispatcher dispatcher = request.getRequestDispatcher("admin-appointments.jsp");
        dispatcher.forward(request, response);
    }
}
