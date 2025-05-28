package br.com.totvs.customermanagement.exception;

public class CpfAlreadyExistsException extends TotvsException {
    public CpfAlreadyExistsException(String cpf) {
        super("CPF " + cpf + " is already registered");
    }
}
