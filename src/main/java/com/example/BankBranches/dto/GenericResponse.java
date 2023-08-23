package com.example.BankBranches.dto;

import lombok.Data;

@Data
public class GenericResponse {
    private int code;
    private String status;
    private String message;
    private Object payLoad;

    public GenericResponse(int code, String status, String message, Object payLoad) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.payLoad = payLoad;
    }

    public GenericResponse(int code, String message, String status) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public GenericResponse(int code, Object payLoad) {
        this.code = code;
        this.payLoad = payLoad;
    }

    public GenericResponse(int code,String message, Object payLoad) {
        this.code = code;
        this.message=message;
        this.payLoad = payLoad;
    }

    public GenericResponse() {
        super();
        System.out.print("hi");
    }
}
