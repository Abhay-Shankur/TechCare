package com.techcare.findmydr.modules;

public class Patient {
    /*---------- Init Variables -------------------- */
    String patientUid;
    String patientName;
    String patientEmail;
    String patientPassword;
    String patientBirth;
    String patientGender;
    String patientPhone;
    String nextFollowUp;
    String patientFirestore;

    public String getPatientFirestore() {
        return patientFirestore;
    }

    public void setPatientFirestore(String patientFirestore) {
        this.patientFirestore = patientFirestore;
    }

    /*---------- Constructor -------------------- */
    public Patient() {
    }

    /*---------- Methods -------------------- */
    // Getters And Setters

    public String getPatientUid() {
        return patientUid;
    }
    public void setPatientUid(String patientUid) {
        this.patientUid = patientUid;
    }

    public String getPatientName() {
        return patientName;
    }
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientEmail() {
        return patientEmail;
    }
    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getPatientPassword() {
        return patientPassword;
    }
    public void setPatientPassword(String patientPassword) {
        this.patientPassword = patientPassword;
    }

    public String getPatientBirth() {
        return patientBirth;
    }
    public void setPatientBirth(String patientBirth) {
        this.patientBirth = patientBirth;
    }

    public String getPatientGender() {
        return patientGender;
    }
    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public String getPatientPhone() {
        return patientPhone;
    }
    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String getNextFollowUp() {
        return nextFollowUp;
    }
    public void setNextFollowUp(String nextFollowUp) {
        this.nextFollowUp = nextFollowUp;
    }
}
