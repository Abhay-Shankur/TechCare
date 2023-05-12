package com.techcare.findmydr.api.response;



import com.techcare.findmydr.api.tablesclass.TablePatient;

import java.util.List;

public class ResponsePatient {
    /*---------- Init Variables -------------------- */
    String statusCode;
    String statusMessage;
    List<TablePatient> dataList;

    /*---------- Constructor -------------------- */
    public ResponsePatient() {
    }

    public ResponsePatient(String statusCode, String statusMessage, List<TablePatient> dataList) {
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

    public List<TablePatient> getDataList() {
        return dataList;
    }
    public void setDataList(List<TablePatient> dataList) {
        this.dataList = dataList;
    }
}
