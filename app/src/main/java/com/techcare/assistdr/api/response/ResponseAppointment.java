package com.techcare.assistdr.api.response;

import com.techcare.assistdr.api.tablesclass.TableAppointmentDetails;

import java.util.List;

public class ResponseAppointment {
    /*---------- Init Variables -------------------- */
    String statusCode;
    String statusMessage;
    List<TableAppointmentDetails> dataList;

    /*---------- Constructor -------------------- */
    public ResponseAppointment() {
    }
    public ResponseAppointment(String statusCode, String statusMessage, List<TableAppointmentDetails> dataList) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.dataList = dataList;
    }
    /*---------- Methods -------------------- */
    // Getters And Setters

    public String getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public List<TableAppointmentDetails> getDataList() {
        return dataList;
    }
    public void setDataList(List<TableAppointmentDetails> dataList) {
        this.dataList = dataList;
    }
}
