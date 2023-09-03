package com.techcare.assistdr.modules;

import java.util.HashMap;

public class AppointmentDetails {
    /*---------- Init Variables -------------------- */
    String appointmentId;
    String prescriptionId;
    String appointmentname;
    String appointmentbirthdate;
    String appointmentgender;
    String appointmentphone;
    String appointmentSchedule;
    HashMap<String,Object> liveLocation;
    boolean isEmergency;


    /*---------- Constructor -------------------- */
    public AppointmentDetails() {
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
