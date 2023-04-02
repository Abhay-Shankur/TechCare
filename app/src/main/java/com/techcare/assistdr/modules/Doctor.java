package com.techcare.assistdr.modules;

public class Doctor {
    private String doctorName;
    private String doctorEmail;
    private String doctorPassword;
    private String doctorPhone;
    private String doctorProfilepic;
    private String doctorEducation;
    private String doctorSpecialis;
    private String doctorHomeTown;
    private String doctorRating;
    private String totalRating;
    private String totalReviewer;

    public Doctor() {
    }

    public Doctor(String name, String email, String phone, String enPass) {
        this.doctorName=name;
        this.doctorEmail=email;
        this.doctorPhone=phone;
        this.doctorPassword = enPass;
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

    public String getDoctorPhone() {
        return doctorPhone;
    }
    public void setDoctorPhone(String doctorPhone) {
        this.doctorPhone = doctorPhone;
    }

    public String getDoctorProfilepic() {
        return doctorProfilepic;
    }
    public void setDoctorProfilepic(String doctorProfilepic) {
        this.doctorProfilepic = doctorProfilepic;
    }

    public String getDoctorEducation() {
        return doctorEducation;
    }
    public void setDoctorEducation(String doctorEducation) {
        this.doctorEducation = doctorEducation;
    }

    public String getDoctorSpecialis() {
        return doctorSpecialis;
    }
    public void setDoctorSpecialis(String doctorSpecialis) {
        this.doctorSpecialis = doctorSpecialis;
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

    public String getTotalRating() {
        return totalRating;
    }
    public void setTotalRating(String totalRating) {
        this.totalRating = totalRating;
    }

    public String getTotalReviewer() {
        return totalReviewer;
    }
    public void setTotalReviewer(String totalReviewer) {
        this.totalReviewer = totalReviewer;
    }
}
