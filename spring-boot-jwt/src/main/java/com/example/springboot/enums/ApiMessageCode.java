package com.example.springboot.enums;

public enum ApiMessageCode {
    SUCCESS(0, "Successful"),
    INVALID_DATA(-1, "Invalid data"),
    INVALID_TOKEN(-2, "Invalid token");

    private final int error;
    private final String msg;

    private ApiMessageCode(int error, String msg) {
        this.error = error;
        this.msg = msg;
    }

    public int getError() {
     return this.error;
    }
    public String getMsg() {
        return this.msg;
    }

    public static ApiMessageCode findByValue(int value) {
        ApiMessageCode result = INVALID_DATA;
        for(ApiMessageCode msgCode : ApiMessageCode.values()) {
            if(msgCode.getError() == value) {
                result = msgCode;
                break;
            }
        }
        return result;
    }
}
