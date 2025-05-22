public class Appointment {
    private String appointmentId;
    private String clientName;
    private String agentName;
    private String date;
    private String time;

    public Appointment(String appointmentId, String clientName, String agentName, String date, String time) {
        this.appointmentId = appointmentId;
        this.clientName = clientName;
        this.agentName = agentName;
        this.date = date;
        this.time = time;
    }

    public Appointment() {}

    public String getAppointmentId() {
        return appointmentId;
    }

    public String getClientName() {
        return clientName;
    }

    public String getAgentName() {
        return agentName;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

