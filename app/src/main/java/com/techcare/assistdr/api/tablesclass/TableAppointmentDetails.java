package com.techcare.assistdr.api.tablesclass;

public class TableAppointmentDetails {
    /*---------- Init Variables -------------------- */
    String appointmentId;
    String appointmentname;
    String appointmentbirthdate;
    String appointmentgender;
    String appointmentphone;
    String appointmentSchedule;

    /*---------- Constructor -------------------- */
    public TableAppointmentDetails() {
    }

    /*---------- Methods -------------------- */
    // Getters And Setters

    public String getAppointmentId() {
        return appointmentId;
    }
    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getAppointmentname() {
        return appointmentname;
    }
    public void setAppointmentname(String appointmentname) {
        this.appointmentname = appointmentname;
    }

    public String getAppointmentbirthdate() {
        return appointmentbirthdate;
    }
    public void setAppointmentbirthdate(String appointmentbirthdate) {
        this.appointmentbirthdate = appointmentbirthdate;
    }

    public String getAppointmentgender() {
        return appointmentgender;
    }
    public void setAppointmentgender(String appointmentgender) {
        this.appointmentgender = appointmentgender;
    }

    public String getAppointmentphone() {
        return appointmentphone;
    }
    public void setAppointmentphone(String appointmentphone) {
        this.appointmentphone = appointmentphone;
    }

    public String getAppointmentSchedule() {
        return appointmentSchedule;
    }
    public void setAppointmentSchedule(String appointmentSchedule) {
        this.appointmentSchedule = appointmentSchedule;
    }
}
