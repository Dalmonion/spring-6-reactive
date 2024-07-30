package com.example.spring6reactive.repository;

import com.example.spring6reactive.domain.Customer;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {
}
