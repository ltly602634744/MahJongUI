package com.example.mahjong.ErrorResponse;


public class ErrorResponse {

    private int status;

    private String message;

    private long timeStamp;

    ErrorResponse(){}

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    //    public StockOutErrorResponse(int status, String message, long timeStamp) {
//        this.status = status;
//        this.message = message;
//        this.timeStamp = timeStamp;
//    }
}
