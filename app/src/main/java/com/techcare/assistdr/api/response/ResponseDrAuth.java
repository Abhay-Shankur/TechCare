package com.techcare.assistdr.api.response;

import com.techcare.assistdr.api.tablesclass.TableDrAuth;

import java.util.List;

public class ResponseDrAuth {
/*---------- Init Variables -------------------- */
    String statusCode;
    String statusMessage;
    List<TableDrAuth> dataList;

/*---------- Constructor -------------------- */
    public ResponseDrAuth() {
    }

    public ResponseDrAuth(String statusCode, String statusMessage, List<TableDrAuth> dataList) {
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

    public List<TableDrAuth> getDataList() {
        return dataList;
    }
    public void setDataList(List<TableDrAuth> dataList) {
        this.dataList = dataList;
    }

}
