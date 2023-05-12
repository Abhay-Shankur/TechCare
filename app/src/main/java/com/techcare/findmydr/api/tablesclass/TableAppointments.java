package com.techcare.findmydr.api.tablesclass;

public class TableAppointments {
    /*---------- Init Variables -------------------- */
    String appointmentId;
    String patientUid;
    String doctorUid;
    String appointmentStatus;
    String appointmentSchedule;

    /*---------- Constructor -------------------- */
    public TableAppointments() {
    }

    /*---------- Methods -------------------- */
    // Getters And Setters

    public String getAppointmentId() {
        return appointmentId;
    }
    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPatientUid() {
        return patientUid;
    }
    public void setPatientUid(String patientUid) {
        this.patientUid = patientUid;
    }

    public String getDoctorUid() {
        return doctorUid;
    }
    public void setDoctorUid(String doctorUid) {
        this.doctorUid = doctorUid;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }
    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public String getAppointmentSchedule() {
        return appointmentSchedule;
    }
    public void setAppointmentSchedule(String appointmentSchedule) {
        this.appointmentSchedule = appointmentSchedule;
    }
}
