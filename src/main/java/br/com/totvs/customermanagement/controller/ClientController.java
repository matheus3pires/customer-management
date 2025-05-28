package br.com.totvs.customermanagement.controller;

import br.com.totvs.customermanagement.payload.request.ClientRequestPayload;
import br.com.totvs.customermanagement.payload.response.ClientResponsePayload;
import br.com.totvs.customermanagement.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/totvs/clients")
@CrossOrigin(origins = "http://localhost:4200")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Operation(summary = "Create a new client")
    @ApiResponse(responseCode = "201", description = "Client created successfully")
    @PostMapping
    public ResponseEntity<ClientResponsePayload> createClient(
            @Valid @RequestBody ClientRequestPayload clientRequestPayload) {
        ClientResponsePayload response = this.clientService.createClient(clientRequestPayload);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get all clients")
    @ApiResponse(responseCode = "200", description = "List of clients returned successfully")
    @GetMapping
    public ResponseEntity<List<ClientResponsePayload>> getAllClients() {
        List<ClientResponsePayload> response = this.clientService.getAllClients();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update a client by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client updated successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @PutMapping("/{cpf}")
    public ResponseEntity<ClientResponsePayload> updateClient(
            @PathVariable String cpf,
            @Valid @RequestBody ClientRequestPayload payload) {
        ClientResponsePayload response = this.clientService.updateClient(cpf, payload);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a client by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Client deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> deleteClient(@PathVariable String cpf) {
        this.clientService.deleteClient(cpf);
        return ResponseEntity.noContent().build();
    }
}
