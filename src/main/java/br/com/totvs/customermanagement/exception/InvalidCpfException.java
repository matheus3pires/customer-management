package br.com.totvs.customermanagement.exception;

public class InvalidCpfException extends TotvsException {
    public InvalidCpfException(String cpf) {
        super("CPF " + cpf + " is invalid");
    }
}