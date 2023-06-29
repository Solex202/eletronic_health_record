package com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions;

public enum ExceptionMessages {
    EMAIL_ALREADY_EXCEPTION("Email already exist"),
    EMAIL_IS_INVALID("Please provide a valid email"),
    PATIENT_WITH_EMAIL_DOESNOT_EXIST("Patient with email %s does not exist"),

    PATIENT_WITH_ID_DOESNOT_EXIST("Patient with id %s does not exist"),


    ;
    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }

    public String getMessage(){
        return  message;
    }
}
