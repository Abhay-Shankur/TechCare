package com.techcare.assistdr.api.tablesclass;

public class TableDrAuth {

/*---------- Init Variables -------------------- */
    String doctorUid;
    String doctorName;
    String doctorEmail;
    String doctorPassword;

/*---------- Constructor -------------------- */
    public TableDrAuth() {
    }

/*---------- Methods -------------------- */
    // Getters And Setters

    public String getDoctorUid() {
        return doctorUid;
    }
    public void setDoctorUid(String doctorUid) {
        this.doctorUid = doctorUid;
    }

    public String getDoctorName() {
        return doctorName;
    }
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }
    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }

    public String getDoctorPassword() {
        return doctorPassword;
    }
    public void setDoctorPassword(String doctorPassword) {
        this.doctorPassword = doctorPassword;
    }
}
