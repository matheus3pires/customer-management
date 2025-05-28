package br.com.totvs.customermanagement.service;

import br.com.totvs.customermanagement.exception.CpfAlreadyExistsException;
import br.com.totvs.customermanagement.exception.CpfNotFoundException;
import br.com.totvs.customermanagement.exception.InvalidCpfException;
import br.com.totvs.customermanagement.exception.NameAlreadyExistsException;
import br.com.totvs.customermanagement.exception.PhoneNumberAlreadyExistsException;
import br.com.totvs.customermanagement.model.Address;
import br.com.totvs.customermanagement.model.Client;
import br.com.totvs.customermanagement.model.Phone;
import br.com.totvs.customermanagement.payload.request.ClientRequestPayload;
import br.com.totvs.customermanagement.payload.request.PhoneRequestPayload;
import br.com.totvs.customermanagement.repository.AddressRepository;
import br.com.totvs.customermanagement.repository.ClientRepository;
import br.com.totvs.customermanagement.repository.PhoneRepository;
import br.com.totvs.customermanagement.util.NumberUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PhoneRepository phoneRepository;

    @Mock
    private AddressRepository addressRepository;

    private ClientService clientService;

    @BeforeEach
    void setup() {
        clientService = new ClientService(clientRepository, phoneRepository, addressRepository);
    }

    @Test
    void createClient_shouldThrowInvalidCpfException_whenCpfInvalid() {
        ClientRequestPayload request = new ClientRequestPayload(
                "Matheus Pires",
                "12345678900",
                List.of(new PhoneRequestPayload("999999999")),
                List.of()
        );

        InvalidCpfException thrown = assertThrows(InvalidCpfException.class,
                () -> clientService.createClient(request));
        assertTrue(thrown.getMessage().contains("12345678900"));
    }

    @Test
    void createClient_shouldThrowCpfAlreadyExistsException_whenCpfExists() {
        ClientRequestPayload request = new ClientRequestPayload(
                "Matheus Pires",
                "11111111111",
                List.of(new PhoneRequestPayload("999999999")),
                List.of()
        );

        when(clientRepository.existsClientByCpf("11111111111")).thenReturn(true);

        try (MockedStatic<NumberUtil> mockedStatic = Mockito.mockStatic(NumberUtil.class)) {
            mockedStatic.when(() -> NumberUtil.isValidCpf("11111111111")).thenReturn(true);

            CpfAlreadyExistsException ex = assertThrows(CpfAlreadyExistsException.class,
                    () -> clientService.createClient(request));

            assertTrue(ex.getMessage().contains("11111111111"));
        }
    }

    @Test
    void createClient_shouldThrowNameAlreadyExistsException_whenNameExists() {
        ClientRequestPayload request = new ClientRequestPayload(
                "ExistingName",
                "11111111111",
                List.of(new PhoneRequestPayload("999999999")),
                List.of()
        );

        when(clientRepository.existsClientByName("ExistingName")).thenReturn(true);
        when(clientRepository.existsClientByCpf("11111111111")).thenReturn(false);

        try (MockedStatic<NumberUtil> mockedStatic = Mockito.mockStatic(NumberUtil.class)) {
            mockedStatic.when(() -> NumberUtil.isValidCpf("11111111111")).thenReturn(true);

            NameAlreadyExistsException ex = assertThrows(NameAlreadyExistsException.class,
                    () -> clientService.createClient(request));

            assertTrue(ex.getMessage().contains("ExistingName"));
        }
    }

    @Test
    void createClient_shouldThrowPhoneNumberAlreadyExistsException_whenPhoneExists() {
        ClientRequestPayload request = new ClientRequestPayload(
                "NewName",
                "11111111111",
                List.of(new PhoneRequestPayload("999999999")),
                List.of()
        );

        when(clientRepository.existsClientByCpf("11111111111")).thenReturn(false);
        when(clientRepository.existsClientByName("NewName")).thenReturn(false);
        when(phoneRepository.existsPhoneByNumber("999999999")).thenReturn(true);

        try (MockedStatic<NumberUtil> mockedStatic = Mockito.mockStatic(NumberUtil.class)) {
            mockedStatic.when(() -> NumberUtil.isValidCpf("11111111111")).thenReturn(true);

            PhoneNumberAlreadyExistsException ex = assertThrows(PhoneNumberAlreadyExistsException.class,
                    () -> clientService.createClient(request));

            assertTrue(ex.getMessage().contains("999999999"));
        }
    }

    @Test
    void createClient_shouldCreateSuccessfully() {
        ClientRequestPayload request = new ClientRequestPayload(
                "NewName",
                "11111111111",
                List.of(new PhoneRequestPayload("999999999")),
                List.of()
        );

        when(clientRepository.existsClientByCpf("11111111111")).thenReturn(false);
        when(clientRepository.existsClientByName("NewName")).thenReturn(false);
        when(phoneRepository.existsPhoneByNumber(anyString())).thenReturn(false);
        Client savedClient = Client.builder().name("NewName").cpf("11111111111").build();
        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);

        try (MockedStatic<NumberUtil> mockedStatic = Mockito.mockStatic(NumberUtil.class)) {
            mockedStatic.when(() -> NumberUtil.isValidCpf("11111111111")).thenReturn(true);

            var response = clientService.createClient(request);

            assertEquals("NewName", response.name());
            assertEquals("11111111111", response.cpf());
            assertEquals(1, response.phones().size());
            assertEquals("999999999", response.phones().get(0).number());

            verify(clientRepository).save(any(Client.class));
            verify(phoneRepository).saveAll(anyList());
            verify(addressRepository).saveAll(anyList());
        }
    }

    @Test
    void getAllClients_shouldReturnClients() {
        Client client = Client.builder().name("Name").cpf("11111111111").build();
        Phone phone = Phone.builder().number("999999999").client(client).build();
        Address address = Address.builder().street("Street").city("City").state("State").zipCode("12345").client(client).build();

        when(clientRepository.findAll()).thenReturn(List.of(client));
        when(phoneRepository.findByClient(client)).thenReturn(List.of(phone));
        when(addressRepository.findByClient(client)).thenReturn(List.of(address));

        var clients = clientService.getAllClients();

        assertEquals(1, clients.size());
        var c = clients.get(0);
        assertEquals("Name", c.name());
        assertEquals("11111111111", c.cpf());
        assertEquals(1, c.phones().size());
        assertEquals("999999999", c.phones().get(0).number());
        assertEquals(1, c.addresses().size());
        assertEquals("Street", c.addresses().get(0).street());
    }

    @Test
    void updateClient_shouldThrowCpfNotFoundException_whenClientDoesNotExist() {
        when(clientRepository.findByCpf("11111111111")).thenReturn(Optional.empty());

        ClientRequestPayload request = new ClientRequestPayload("NewName", "11111111111", List.of(), List.of());

        assertThrows(CpfNotFoundException.class, () -> clientService.updateClient("11111111111", request));
    }

    @Test
    void updateClient_shouldUpdateSuccessfully() {
        Client existingClient = Client.builder().name("OldName").cpf("11111111111").build();
        when(clientRepository.findByCpf("11111111111")).thenReturn(Optional.of(existingClient));
        when(clientRepository.findNameByCpf("11111111111")).thenReturn(Optional.of("OldName"));
        when(clientRepository.existsClientByName("NewName")).thenReturn(false);
        when(phoneRepository.existsPhoneByNumber(anyString())).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenReturn(existingClient);

        ClientRequestPayload request = new ClientRequestPayload(
                "NewName",
                "11111111111",
                List.of(new PhoneRequestPayload("999999999")),
                List.of()
        );

        var response = clientService.updateClient("11111111111", request);

        assertEquals("NewName", response.name());
        assertEquals("11111111111", response.cpf());
        assertEquals(1, response.phones().size());
        assertEquals("999999999", response.phones().get(0).number());

        verify(phoneRepository, times(2)).deleteAllByClient(existingClient);
        verify(phoneRepository).saveAll(anyList());
        verify(addressRepository).deleteAllByClient(existingClient);
        verify(addressRepository).saveAll(anyList());
    }

    @Test
    void deleteClient_shouldThrowCpfNotFoundException_whenClientNotFound() {
        when(clientRepository.findByCpf("11111111111")).thenReturn(Optional.empty());

        assertThrows(CpfNotFoundException.class, () -> clientService.deleteClient("11111111111"));
    }

    @Test
    void deleteClient_shouldDeleteSuccessfully() {
        Client client = Client.builder().name("Name").cpf("11111111111").build();
        when(clientRepository.findByCpf("11111111111")).thenReturn(Optional.of(client));

        clientService.deleteClient("11111111111");

        verify(phoneRepository).deleteAllByClient(client);
        verify(addressRepository).deleteAllByClient(client);
        verify(clientRepository).delete(client);
    }
}