package br.com.totvs.customermanagement.service;

import br.com.totvs.customermanagement.exception.ClientNotFoundException;
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
import br.com.totvs.customermanagement.payload.response.AddressResponsePayload;
import br.com.totvs.customermanagement.payload.response.ClientResponsePayload;
import br.com.totvs.customermanagement.payload.response.PhoneResponsePayload;
import br.com.totvs.customermanagement.repository.AddressRepository;
import br.com.totvs.customermanagement.repository.ClientRepository;
import br.com.totvs.customermanagement.repository.PhoneRepository;
import br.com.totvs.customermanagement.util.NumberUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final PhoneRepository phoneRepository;
    private final AddressRepository addressRepository;

    public ClientService(ClientRepository clientRepository, PhoneRepository phoneRepository,
                         AddressRepository addressRepository) {
        this.clientRepository = clientRepository;
        this.phoneRepository = phoneRepository;
        this.addressRepository = addressRepository;
    }

    /**
     * Creates a new client with associated phones and addresses.
     * Validates the client data before saving.
     *
     * @param request the client data to be created
     * @return the created client data as a response payload
     * @throws InvalidCpfException if the CPF is invalid
     * @throws CpfAlreadyExistsException if the CPF already exists in the system
     * @throws NameAlreadyExistsException if the client name already exists
     * @throws PhoneNumberAlreadyExistsException if any phone number already exists
     */
    @Transactional
    public ClientResponsePayload createClient(ClientRequestPayload request) {
        validateClientCreate(request.name(), request.cpf(), request.phones());

        final Client client = this.clientRepository.save(Client.builder().name(request.name()).cpf(request.cpf()).build());

        List<Phone> phones = request.phones().stream()
            .map(phoneRequest -> Phone.builder()
                    .number(phoneRequest.number())
                    .client(client)
                    .build())
            .collect(Collectors.toList());

        this.phoneRepository.saveAll(phones);

        List<Address> addresses = request.addresses().stream()
                .map(addressRequest -> Address.builder()
                        .street(addressRequest.street())
                        .complement(addressRequest.complement())
                        .city(addressRequest.city())
                        .state(addressRequest.state())
                        .zipCode(addressRequest.zipCode())
                        .client(client)
                        .build())
                .collect(Collectors.toList());

        this.addressRepository.saveAll(addresses);

        return new ClientResponsePayload(client.getName(), client.getCpf(),
                phones.stream().map(phone -> new PhoneResponsePayload(phone.getNumber())).toList(),
                addresses.stream().map(address -> new AddressResponsePayload(address.getStreet(),
                        address.getComplement(), address.getCity(), address.getState(), address.getZipCode()
                )).toList()
        );
    }

    /**
     * Retrieves a client by their CPF and returns a response payload containing their details,
     * including associated phone numbers and addresses.
     *
     * @param cpf the CPF (Cadastro de Pessoa Física) identifier of the client.
     * @return a {@link ClientResponsePayload} object containing the client's name, CPF,
     *         a list of their phone numbers, and a list of their addresses.
     * @throws ClientNotFoundException if no client is found with the given CPF.
     */
    public ClientResponsePayload getClientByCpf(String cpf) {
        Client client = this.clientRepository.findByCpf(cpf)
                .orElseThrow(() -> new ClientNotFoundException(cpf));

        return new ClientResponsePayload(
                client.getName(),
                client.getCpf(),
                this.phoneRepository.findByClient(client).stream().map(phone ->
                        new PhoneResponsePayload(phone.getNumber())).toList(),
                this.addressRepository.findByClient(client).stream().map(
                        address -> new AddressResponsePayload(
                                address.getStreet(), address.getComplement(), address.getCity(),
                                address.getState(), address.getZipCode())).toList());
    }

    /**
     * Retrieves all clients with their phones and addresses.
     *
     * @return a list of all clients with detailed information
     */
    @Transactional()
    public List<ClientResponsePayload> getAllClients() {
        List<Client> clients = this.clientRepository.findAll();

        return clients.stream().map(client -> {
            List<Phone> phones = this.phoneRepository.findByClient(client);
            List<Address> addresses = this.addressRepository.findByClient(client);

            return new ClientResponsePayload(
                    client.getName(),
                    client.getCpf(),
                    phones.stream().map(phone -> new PhoneResponsePayload(phone.getNumber())).toList(),
                    addresses.stream().map(address -> new AddressResponsePayload(
                            address.getStreet(), address.getComplement(), address.getCity(),
                            address.getState(), address.getZipCode()
                    )).toList()
            );
        }).toList();
    }

    /**
     * Updates an existing client identified by CPF with new data.
     * Validates updates to ensure data integrity.
     *
     * @param cpf the CPF of the client to update
     * @param request the new client data
     * @return the updated client data as a response payload
     * @throws CpfNotFoundException if the client with the given CPF does not exist
     * @throws NameAlreadyExistsException if the new name already exists for another client
     * @throws CpfAlreadyExistsException if the new CPF already exists for another client
     * @throws PhoneNumberAlreadyExistsException if any phone number is already in use by another client
     */
    @Transactional
    public ClientResponsePayload updateClient(String cpf, ClientRequestPayload request) {
        Client existingClient = this.clientRepository.findByCpf(cpf)
                .orElseThrow(() -> new CpfNotFoundException(cpf));

        validateClientOnUpdate(cpf, request.name(), request.cpf(), request.phones());

        existingClient.setName(request.name());
        existingClient.setCpf(request.cpf());
        final Client client = this.clientRepository.save(existingClient);

        this.phoneRepository.deleteAllByClient(client);
        this.phoneRepository.deleteAllByClient(client);
        List<Phone> phones = request.phones().stream()
                .map(phoneRequest -> Phone.builder()
                        .number(phoneRequest.number())
                        .client(client)
                        .build())
                .toList();
        this.phoneRepository.saveAll(phones);

        this.addressRepository.deleteAllByClient(client);
        List<Address> addresses = request.addresses().stream()
                .map(addressRequest -> Address.builder()
                        .street(addressRequest.street())
                        .complement(addressRequest.complement())
                        .city(addressRequest.city())
                        .state(addressRequest.state())
                        .zipCode(addressRequest.zipCode())
                        .client(client)
                        .build())
                .toList();
        this.addressRepository.saveAll(addresses);

        return new ClientResponsePayload(
                client.getName(),
                client.getCpf(),
                phones.stream()
                        .map(phone -> new PhoneResponsePayload(phone.getNumber()))
                        .toList(),
                addresses.stream()
                        .map(address -> new AddressResponsePayload(
                                address.getStreet(), address.getComplement(), address.getCity(), address.getState(), address.getZipCode()
                        ))
                        .toList()
        );
    }

    /**
     * Deletes a client identified by CPF along with their phones and addresses.
     *
     * @param cpf the CPF of the client to delete
     * @throws CpfNotFoundException if no client with the given CPF exists
     */
    @Transactional
    public void deleteClient(String cpf) {
        Client client = this.clientRepository.findByCpf(cpf)
                .orElseThrow(() -> new CpfNotFoundException(cpf));

        this.phoneRepository.deleteAllByClient(client);
        this.addressRepository.deleteAllByClient(client);
        this.clientRepository.delete(client);
    }

    /**
     * Validates client data before creation.
     *
     * @param name client name
     * @param cpf client CPF
     * @param phones list of phones associated with client
     * @throws InvalidCpfException if CPF is invalid
     * @throws CpfAlreadyExistsException if CPF is already registered
     * @throws NameAlreadyExistsException if client name already exists
     * @throws PhoneNumberAlreadyExistsException if any phone number already exists
     */
    private void validateClientCreate(String name, String cpf, List<PhoneRequestPayload> phones) {
        if (!NumberUtil.isValidCpf(cpf)) {
            throw new InvalidCpfException(cpf);
        }
        if (this.clientRepository.existsClientByCpf(cpf)) {
            throw new CpfAlreadyExistsException(cpf);
        }
        if (this.clientRepository.existsClientByName(name)) {
            throw new NameAlreadyExistsException(name);
        }

        for (PhoneRequestPayload phone : phones) {
            if (this.phoneRepository.existsPhoneByNumber((phone.number()))) {
                throw new PhoneNumberAlreadyExistsException(phone.number());
            }
        }
    }

    /**
     * Validates client data during update operation.
     *
     * @param originalCpf the original CPF of the client being updated
     * @param newName the new client name
     * @param newCpf the new CPF
     * @param phones list of phones associated with client
     * @throws NameAlreadyExistsException if new name already exists for another client
     * @throws CpfAlreadyExistsException if new CPF already exists for another client
     * @throws PhoneNumberAlreadyExistsException if any phone number is in use by another client
     */
    private void validateClientOnUpdate(String originalCpf, String newName, String newCpf, List<PhoneRequestPayload> phones) {
        if (!newName.equals(this.clientRepository.findNameByCpf(originalCpf).orElse(null))
                && this.clientRepository.existsClientByName(newName)) {
            throw new NameAlreadyExistsException(newName);
        }

        if (!newCpf.equals(originalCpf) && this.clientRepository.existsClientByCpf(newCpf)) {
            throw new CpfAlreadyExistsException(newCpf);
        }

        for (PhoneRequestPayload phone : phones) {
            if (this.phoneRepository.existsPhoneByNumber(phone.number())) {
                Optional<Client> existingClient = this.phoneRepository.findClientByPhoneNumber(phone.number());
                if (existingClient.isEmpty() || !existingClient.get().getCpf().equals(originalCpf)) {
                    throw new PhoneNumberAlreadyExistsException(phone.number());
                }
            }
        }
    }

}
