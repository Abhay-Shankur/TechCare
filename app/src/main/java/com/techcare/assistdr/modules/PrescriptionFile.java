package com.techcare.assistdr.modules;

public class PrescriptionFile {
    private String name;
    private String id;
    private String date;
    private String time;
    private String filePath;

    public PrescriptionFile() {
    }

    public PrescriptionFile(String name, String id, String date, String time, String filePath) {
        this.name = name;
        this.id = id;
        this.date = date;
        this.time = time;
        this.filePath= filePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
