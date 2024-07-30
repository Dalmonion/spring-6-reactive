package com.example.spring6reactive.controller;

import com.example.spring6reactive.model.CustomerDTO;
import com.example.spring6reactive.service.CustomerService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    public static final String CUSTOMER_PATH = "/api/v2/customer";
    public static final String CUSTOMER_PATH_BY_ID = CUSTOMER_PATH + "/{customerId}";

    private final CustomerService customerService;

    @DeleteMapping(CUSTOMER_PATH_BY_ID)
    Mono<ResponseEntity<Void>> deleteById(@PathVariable Integer customerId) {
        return customerService.deleteCustomerById(customerId)
                .map(response -> ResponseEntity.noContent().build());
    }

    @PatchMapping(CUSTOMER_PATH_BY_ID)
    Mono<ResponseEntity<Void>> patchCustomer(@PathVariable Integer customerId, @RequestBody CustomerDTO customerDTO) {
        return customerService.patchCustomer(customerId, customerDTO)
                .map(updatedDto -> ResponseEntity.ok().build());
    }

    @PutMapping(CUSTOMER_PATH_BY_ID)
    Mono<ResponseEntity<Void>> updateExistingCustomer(@PathVariable Integer customerId,
                                                      @Validated @RequestBody CustomerDTO customerDTO) {
        return customerService.updateCustomer(customerId, customerDTO)
                .map(updatedCustomer -> ResponseEntity.ok().build());
    }

    @PostMapping(CUSTOMER_PATH)
    Mono<ResponseEntity<Void>> createCustomer(@Validated @RequestBody CustomerDTO customerDTO) {
        return customerService.createNewCustomer(customerDTO)
                .map(savedDto -> ResponseEntity.created(UriComponentsBuilder
                                                                .fromHttpUrl("http://localhost:8080/"
                                                                                     + CUSTOMER_PATH
                                                                                     + "/" + savedDto.getId())
                                                                .build().toUri())
                        .build());
    }

    @GetMapping(CUSTOMER_PATH_BY_ID)
    Mono<CustomerDTO> getCustomerById(@PathVariable Integer customerId) {
        return customerService.getCustomerById(customerId);
    }

    @GetMapping(CUSTOMER_PATH)
    Flux<CustomerDTO> listCustomers() {
        return customerService.listCustomers();
    }
}