package br.com.totvs.customermanagement.exception;

public class CpfNotFoundException extends TotvsException {
    public CpfNotFoundException(String cpf) {
        super("CPF " + cpf + " was not found");
    }
}
