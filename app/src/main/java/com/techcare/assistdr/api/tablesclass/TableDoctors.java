package com.techcare.assistdr.api.tablesclass;

public class TableDoctors {
    /*---------- Init Variables -------------------- */
    String doctorUid;
    String doctorName;
    String doctorProfilepic;
    String doctorSpecialis;
    String doctorEducation;
    String doctorHomeTown;
    String doctorRating;

    /*---------- Constructor -------------------- */

    public TableDoctors() {
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

    public String getDoctorProfilepic() {
        return doctorProfilepic;
    }
    public void setDoctorProfilepic(String doctorProfilepic) {
        this.doctorProfilepic = doctorProfilepic;
    }

    public String getDoctorSpecialis() {
        return doctorSpecialis;
    }
    public void setDoctorSpecialis(String doctorSpecialis) {
        this.doctorSpecialis = doctorSpecialis;
    }

    public String getDoctorEducation() {
        return doctorEducation;
    }
    public void setDoctorEducation(String doctorEducation) {
        this.doctorEducation = doctorEducation;
    }

    public String getDoctorHomeTown() {
        return doctorHomeTown;
    }
    public void setDoctorHomeTown(String doctorHomeTown) {
        this.doctorHomeTown = doctorHomeTown;
    }

    public String getDoctorRating() {
        return doctorRating;
    }
    public void setDoctorRating(String doctorRating) {
        this.doctorRating = doctorRating;
    }
}
