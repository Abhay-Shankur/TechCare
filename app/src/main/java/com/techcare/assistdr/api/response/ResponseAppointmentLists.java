package com.techcare.assistdr.api.response;

import com.techcare.assistdr.api.tablesclass.TableAppointments;

import java.util.List;

public class ResponseAppointmentLists {
    /*---------- Init Variables -------------------- */
    String statusCode;
    String statusMessage;
    List<TableAppointments> dataList;

    /*---------- Constructor -------------------- */
    public ResponseAppointmentLists() {
    }
    public ResponseAppointmentLists(String statusCode, String statusMessage, List<TableAppointments> dataList) {
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

    public List<TableAppointments> getDataList() {
        return dataList;
    }
    public void setDataList(List<TableAppointments> dataList) {
        this.dataList = dataList;
    }
}
