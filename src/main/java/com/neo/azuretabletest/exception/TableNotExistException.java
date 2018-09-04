package com.neo.azuretabletest.exception;

public class TableNotExistException extends Exception{

    public TableNotExistException(String message) {
        super(message);
    }
}
