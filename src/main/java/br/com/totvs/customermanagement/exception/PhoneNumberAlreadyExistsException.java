package br.com.totvs.customermanagement.exception;

public class PhoneNumberAlreadyExistsException extends TotvsException {
    public PhoneNumberAlreadyExistsException(String phoneNumber) {
        super("Phone number " + phoneNumber + " is already registered");
    }
}