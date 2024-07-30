package com.example.spring6reactive.service;

import com.example.spring6reactive.mapper.CustomerMapper;
import com.example.spring6reactive.model.CustomerDTO;
import com.example.spring6reactive.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Flux<CustomerDTO> listCustomers() {
        return customerRepository.findAll()
                .map(customerMapper::toCustomerDTO);
    }

    @Override
    public Mono<CustomerDTO> getCustomerById(Integer customerId) {
        return customerRepository.findById(customerId)
                .map(customerMapper::toCustomerDTO);
    }

    @Override
    public Mono<CustomerDTO> createNewCustomer(CustomerDTO customerDTO) {
        return customerRepository.save(customerMapper.toCustomer(customerDTO))
                .map(customerMapper::toCustomerDTO);
    }

    @Override
    public Mono<CustomerDTO> updateCustomer(Integer customerId, CustomerDTO customerDTO) {
        return customerRepository.findById(customerId)
                .map(foundedCustomer -> {
                    foundedCustomer.setCustomerName(customerDTO.getCustomerName());
                    return foundedCustomer;
                }).flatMap(customerRepository::save)
                .map(customerMapper::toCustomerDTO);
    }

    @Override
    public Mono<CustomerDTO> patchCustomer(Integer customerId, CustomerDTO customerDTO) {
        return customerRepository.findById(customerId)
                .map(foundedCustomer -> {
                    if (StringUtils.hasText(customerDTO.getCustomerName())) {
                        foundedCustomer.setCustomerName(customerDTO.getCustomerName());
                    }
                    return foundedCustomer;
                }).flatMap(customerRepository::save)
                .map(customerMapper::toCustomerDTO);
    }

    @Override
    public Mono<Void> deleteCustomerById(Integer customerId) {
        return customerRepository.deleteById(customerId);
    }
}
