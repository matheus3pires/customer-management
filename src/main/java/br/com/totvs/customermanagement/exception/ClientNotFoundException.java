package br.com.totvs.customermanagement.exception;

public class ClientNotFoundException extends TotvsException {
    public ClientNotFoundException(String cpf) {
        super("Client with CPF " + cpf + " not found");
    }
}
