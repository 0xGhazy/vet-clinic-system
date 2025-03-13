package com.vetclinic.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class FiledValidationException extends RuntimeException{
    private String field;
    private String reason;
}
