package com.techcare.assistdr.api.response;

import com.techcare.assistdr.api.tablesclass.TableDoctors;

import java.util.List;

public class ResponseDoctors {
    /*---------- Init Variables -------------------- */
    String statusCode;
    String statusMessage;
    List<TableDoctors> dataList;

    /*---------- Constructor -------------------- */
    public ResponseDoctors() {
    }
    public ResponseDoctors(String statusCode, String statusMessage, List<TableDoctors> dataList) {
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

    public List<TableDoctors> getDataList() {
        return dataList;
    }
    public void setDataList(List<TableDoctors> dataList) {
        this.dataList = dataList;
    }

}
