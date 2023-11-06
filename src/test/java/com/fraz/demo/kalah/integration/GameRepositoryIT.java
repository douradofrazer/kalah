package com.fraz.demo.kalah.integration;

import com.fraz.demo.kalah.domain.KalahGame;
import com.fraz.demo.kalah.repository.GameInMemoryRepository;
import com.fraz.demo.kalah.repository.GameRepository;
import com.fraz.demo.kalah.repository.model.Game;
import com.fraz.demo.kalah.repository.model.Kalah;
import com.fraz.demo.kalah.service.mapper.KalahMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.math.BigInteger;

@Testcontainers
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
      "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration,"
          + "org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration"
    })
public class GameRepositoryIT {

  @MockBean private GameInMemoryRepository gameInMemoryRepository;

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.flyway.enabled", () -> true);
    registry.add("spring.flyway.locations", () -> "classpath:db/migration");
    registry.add("spring.flyway.url", postgres::getJdbcUrl);
    registry.add("spring.flyway.user", postgres::getUsername);
    registry.add("spring.flyway.password", postgres::getPassword);
  }

  @Autowired private KalahMapper kalahMapper;

  @Autowired private GameRepository gameRepository;

  @Test
  void testShouldSaveGame() {
    KalahGame kalahGame = new KalahGame("john_doe");
    kalahGame.joinGame(kalahGame.getGameReference().toString(), "billy_the_kid");
    kalahGame.makeMove(kalahGame.getCurrentPlayer(), 1);
    Kalah kalah = kalahMapper.toKalah(kalahGame);
    Game game = new Game();
    game.setReference(kalah.getGameReference());
    game.setSnapshot(kalah);
    Game savedGame = gameRepository.save(game);
    Assertions.assertEquals(1, gameRepository.count());
    Assertions.assertEquals(1, savedGame.getId());
    Assertions.assertEquals(kalah.getGameReference(), savedGame.getReference());
    Assertions.assertNotNull(savedGame.getSnapshot());
    Assertions.assertNotNull(savedGame.getCreatedAt());
    Assertions.assertEquals(
        kalah.getBoard().getPits()[1].getSeeds(),
        savedGame.getSnapshot().getBoard().getPits()[1].getSeeds());
    Assertions.assertEquals(kalah.getPlayerTwo(), savedGame.getSnapshot().getCurrentPlayer());
  }
}
