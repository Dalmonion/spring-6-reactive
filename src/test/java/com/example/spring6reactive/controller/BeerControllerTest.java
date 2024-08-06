package com.example.spring6reactive.controller;

import com.example.spring6reactive.domain.Beer;
import com.example.spring6reactive.model.BeerDTO;
import com.example.spring6reactive.repository.BeerRepositoryTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.example.spring6reactive.controller.BeerController.BEER_PATH;
import static com.example.spring6reactive.controller.BeerController.BEER_PATH_BY_ID;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
class BeerControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void testDeleteBeerByIdNotFound() {
        webTestClient.mutateWith(mockOAuth2Login())
                .delete().uri(BEER_PATH_BY_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(999)
    void testDeleteBeerById() {
        webTestClient.mutateWith(mockOAuth2Login())
                .delete().uri(BEER_PATH_BY_ID, 1)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testPatchBeerNotFound() {
        Beer testBeer = BeerRepositoryTest.getTestBeer();
        testBeer.setBeerName("new name");

        webTestClient
                .mutateWith(mockOAuth2Login())
                .patch().uri(BEER_PATH_BY_ID, 999)
                .body(Mono.just(testBeer), BeerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(6)
    void testPatchBeer() {
        Beer testBeer = BeerRepositoryTest.getTestBeer();
        testBeer.setBeerName("new name");

        webTestClient.mutateWith(mockOAuth2Login())
                .patch().uri(BEER_PATH_BY_ID, 1)
                .body(Mono.just(testBeer), BeerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @Order(5)
    void testUpdateBeerBadData() {
        Beer testBeer = BeerRepositoryTest.getTestBeer();
        testBeer.setBeerName("");

        webTestClient.mutateWith(mockOAuth2Login())
                .put().uri(BEER_PATH_BY_ID, 1)
                .body(Mono.just(testBeer), BeerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testUpdateBeerNotFound() {
        webTestClient.mutateWith(mockOAuth2Login())
                .put().uri(BEER_PATH_BY_ID, 9999)
                .body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(4)
    void testUpdateBeer() {
        webTestClient.mutateWith(mockOAuth2Login())
                .put().uri(BEER_PATH_BY_ID, 1)
                .body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testCreateBeerBadData() {
        Beer testBeer = BeerRepositoryTest.getTestBeer();
        testBeer.setBeerName("");

        webTestClient.mutateWith(mockOAuth2Login())
                .post().uri(BEER_PATH)
                .body(Mono.just(testBeer), BeerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testCreateBeer() {
        webTestClient.mutateWith(mockOAuth2Login())
                .post().uri(BEER_PATH)
                .body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("http://localhost:8080/api/v2/beer/4");
    }

    @Test
    @Order(1)
    void testListBeers() {
        webTestClient.mutateWith(mockOAuth2Login())
                .get().uri(BEER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody().jsonPath("$.size()").isEqualTo(3);
    }

    @Test
    void testGetBeerByIdNotFound() {
        webTestClient.mutateWith(mockOAuth2Login())
                .get().uri(BEER_PATH_BY_ID, 9999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(2)
    void testGetBeerById() {
        webTestClient.mutateWith(mockOAuth2Login())
                .get().uri(BEER_PATH_BY_ID, 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody(BeerDTO.class);
    }
}