package com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {

    private String message;
    private T data;
    private int statusCode;
    private String token;
}
