package com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions;

public enum ExceptionMessages {
    EMAIL_ALREADY_EXCEPTION("Email already exist"),
    EMAIL_IS_INVALID("Please provide a valid email"),
    PATIENT_WITH_EMAIL_DOESNOT_EXIST("Patient with email %s does not exist"),

    PATIENT_WITH_ID_DOESNOT_EXIST("Patient with id %s does not exist"),

    INVALID_PASSWORD("Password is too weak,password must contain at least 1 upper case, 1 special , 1 number and must be minimum of 8 characters"),

    INCORRECT_EMAIL_OR_PASSWORD("Email or password is incorrect"),

    UPDATE_MESSAGE("Updated Successful");

//    INCORRECT_PASSWORD("Password is incorrect");
    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }

    public String getMessage(){
        return  message;
    }
}
