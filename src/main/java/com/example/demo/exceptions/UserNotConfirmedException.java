package com.example.demo.exceptions;

public class UserNotConfirmedException extends Exception {
    @Override
    public String getMessage() {
        return "devi confermare l'account per poter accedere ";
    }
}
