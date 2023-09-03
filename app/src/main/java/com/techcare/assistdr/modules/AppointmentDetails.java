package com.techcare.assistdr.modules;

import java.util.HashMap;

public class AppointmentDetails {
    /*---------- Init Variables -------------------- */
    String appointmentId;
    String doctorId;
    String prescriptionId;
    String appointmentName;
    String appointmentBirthdate;
    String appointmentGender;
    String appointmentPhone;
    String appointmentSchedule;
    String appointmentDescription;
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

    public String getappointmentName() {
        return appointmentName;
    }
    public void setappointmentName(String appointmentName) {
        this.appointmentName = appointmentName;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getappointmentBirthdate() {
        return appointmentBirthdate;
    }
    public void setappointmentBirthdate(String appointmentBirthdate) {
        this.appointmentBirthdate = appointmentBirthdate;
    }

    public String getappointmentGender() {
        return appointmentGender;
    }
    public void setappointmentGender(String appointmentGender) {
        this.appointmentGender = appointmentGender;
    }

    public String getappointmentPhone() {
        return appointmentPhone;
    }
    public void setappointmentPhone(String appointmentPhone) {
        this.appointmentPhone = appointmentPhone;
    }

    public String getAppointmentSchedule() {
        return appointmentSchedule;
    }
    public void setAppointmentSchedule(String appointmentSchedule) {
        this.appointmentSchedule = appointmentSchedule;
    }

    public String getAppointmentDescription() {
        return appointmentDescription;
    }

    public void setAppointmentDescription(String appointmentDescription) {
        this.appointmentDescription = appointmentDescription;
    }

    public HashMap<String, Object> getLiveLocation() {
        return liveLocation;
    }

    public void setLiveLocation(HashMap<String, Object> liveLocation) {
        this.liveLocation = liveLocation;
    }

    public boolean isEmergency() {
        return isEmergency;
    }

    public void setEmergency(boolean emergency) {
        isEmergency = emergency;
    }
}
