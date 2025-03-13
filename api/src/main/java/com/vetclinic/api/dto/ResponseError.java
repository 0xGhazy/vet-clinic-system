package com.vetclinic.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseError {
    private String message;
    private Object reason;
    private String trxId;
    private LocalDateTime timestamp;
    private Object details;

    @Override
    public String toString() {
        return "{" +
                "message='" + message + '\'' +
                ", reason=" + reason +
                ", trxId='" + trxId + '\'' +
                ", timestamp=" + timestamp +
                ", details=" + details +
                '}';
    }
}