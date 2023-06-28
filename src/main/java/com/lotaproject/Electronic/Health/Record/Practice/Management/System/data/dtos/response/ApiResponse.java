package com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response;

import lombok.Data;

@Data
public class ApiResponse<T> {

    private String message;
    private T data;
    private int statusCode;
}
