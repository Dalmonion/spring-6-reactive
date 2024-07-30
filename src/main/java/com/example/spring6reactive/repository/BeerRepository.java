package com.example.spring6reactive.repository;

import com.example.spring6reactive.domain.Beer;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BeerRepository extends ReactiveCrudRepository<Beer, Integer> {
}
