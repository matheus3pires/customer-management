package br.com.totvs.customermanagement.exception.config;

import br.com.totvs.customermanagement.exception.TotvsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TotvsException.class)
    public ResponseEntity<String> handleCpfAlreadyExists(TotvsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}
