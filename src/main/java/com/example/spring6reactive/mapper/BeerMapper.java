package com.example.spring6reactive.mapper;

import com.example.spring6reactive.domain.Beer;
import com.example.spring6reactive.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    BeerDTO toBeerDTO(Beer beer);
    Beer toBeer(BeerDTO beerDTO);
}
