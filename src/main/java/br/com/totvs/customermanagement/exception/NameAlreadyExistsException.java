package br.com.totvs.customermanagement.exception;

public class NameAlreadyExistsException extends TotvsException {
    public NameAlreadyExistsException(String name) {
        super("Client name '" + name + "' is already registered");
    }
}
