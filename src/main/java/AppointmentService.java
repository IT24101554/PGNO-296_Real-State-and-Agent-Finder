import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentService {
    private final String FILE_PATH = "C:\\Users\\oncex\\Desktop\\Appointment\\Data\\Appointments.txt";

    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    Appointment a = new Appointment(parts[0], parts[1], parts[2], parts[3], parts[4]);
                    appointments.add(a);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    public void addAppointment(Appointment a) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            writer.println(a.getAppointmentId() + "," + a.getClientName() + "," + a.getAgentName() + "," + a.getDate() + "," + a.getTime());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteAppointment(String appointmentId) {
        List<Appointment> appointments = getAllAppointments();
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Appointment a : appointments) {
                if (!a.getAppointmentId().equals(appointmentId)) {
                    writer.println(a.getAppointmentId() + "," + a.getClientName() + "," + a.getAgentName() + "," + a.getDate() + "," + a.getTime());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
