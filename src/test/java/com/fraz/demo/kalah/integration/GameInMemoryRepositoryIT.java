package com.fraz.demo.kalah.integration;

import com.fraz.demo.kalah.domain.constant.GameStatus;
import com.fraz.demo.kalah.repository.GameInMemoryRepository;
import com.fraz.demo.kalah.repository.GameRepository;
import com.fraz.demo.kalah.repository.model.Kalah;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
      "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,"
          + "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration"
    })
public class GameInMemoryRepositoryIT {

  private final String playerOne = "out_law";
  private final String playerTwo = "billy_the_kid";
  @MockBean private GameRepository gameRepository;
  @Autowired private GameInMemoryRepository gameInMemoryRepository;

  @Container
  static RedisContainer REDIS_CONTAINER =
      new RedisContainer(DockerImageName.parse("redis:5.0.3-alpine")).withExposedPorts(6379);

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
    registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getFirstMappedPort().toString());
  }

  @Test
  void testShouldCheckIfRedisContainerIsRunning() {
    System.out.println(
        "URL=" + REDIS_CONTAINER.getHost() + REDIS_CONTAINER.getFirstMappedPort().toString());
    assertTrue(REDIS_CONTAINER.isRunning());
  }

  @Test
  void testShouldSaveKalahGame() {

    Kalah kalah = new Kalah();
    kalah.setGameReference(UUID.randomUUID());
    kalah.setPlayerOne(playerOne);
    kalah.setPlayerTwo(playerTwo);
    kalah.setBoard(new Kalah.Board());
    kalah.setCurrentPlayer(playerOne);
    kalah.setGameStatus(GameStatus.INITIALIZED);
    kalah.setWinner(null);

    gameInMemoryRepository.save(kalah);

    Kalah gameFound = gameInMemoryRepository.findById(kalah.getGameReference()).orElseThrow();

    assertEquals(playerTwo, gameFound.getPlayerTwo());
  }
}
