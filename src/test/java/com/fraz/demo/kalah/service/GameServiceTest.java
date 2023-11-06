package com.fraz.demo.kalah.service;

import com.fraz.demo.kalah.domain.KalahGame;
import com.fraz.demo.kalah.repository.GameInMemoryRepository;
import com.fraz.demo.kalah.repository.model.Kalah;
import com.fraz.demo.kalah.service.mapper.KalahMapper;
import com.fraz.demo.kalah.transferobj.GameDto;
import com.fraz.demo.kalah.transferobj.PlayerDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

  @Mock private GameInMemoryRepository gameInMemoryRepository;

  @Spy private KalahMapper kalahMapper = Mappers.getMapper(KalahMapper.class);

  @InjectMocks private GameServiceImpl gameService;

  private final String playerOne = "john_doe";
  private final String playerTwo = "billy_the_kid";
  private final KalahGame kalahGame = new KalahGame(playerOne);

  @BeforeEach
  void setUp() {}

  @Test
  void testShouldInitializeGame() {
    Kalah kalah = kalahMapper.toKalah(kalahGame);
    when(gameInMemoryRepository.save(any(Kalah.class))).thenReturn(kalah);
    GameDto init = gameService.init(new PlayerDto(playerOne));

    Assertions.assertAll(
        () -> Assertions.assertNotNull(init.gameReference()),
        () -> Assertions.assertEquals(playerOne, init.playerOne()),
        () -> Assertions.assertNull(init.playerTwo()));
  }

  @Test
  void testShouldAllowPlayerTwoToJoinGame() {
    // mock an existing game
    Kalah kalah = kalahMapper.toKalah(kalahGame);
    when(gameInMemoryRepository.findById(any())).thenReturn(Optional.of(kalah));

    // mock the game after player two joins
    kalahGame.joinGame(kalahGame.getGameReference().toString(), playerTwo);
    Kalah kalahWithP2 = kalahMapper.toKalah(kalahGame);
    when(gameInMemoryRepository.save(any(Kalah.class))).thenReturn(kalahWithP2);

    GameDto joinedGame = gameService.join(kalahGame.getGameReference(), new PlayerDto(playerTwo));

    Assertions.assertEquals(playerTwo, joinedGame.playerTwo());
  }

  @Test
  void testShouldNotLetPlayerJoinNonExistentGame() {
    when(gameInMemoryRepository.findById(any())).thenReturn(Optional.empty());
    Assertions.assertThrows(
        RuntimeException.class,
        () -> gameService.join(kalahGame.getGameReference(), new PlayerDto(playerTwo)));
  }

  @Test
  void testShouldNotLetPlayerTwoJoinAFullGame() {
    // mock an existing FULL game
    kalahGame.joinGame(kalahGame.getGameReference().toString(), playerTwo);
    Kalah kalah = kalahMapper.toKalah(kalahGame);
    when(gameInMemoryRepository.findById(any())).thenReturn(Optional.of(kalah));

    RuntimeException runtimeException =
        Assertions.assertThrows(
            RuntimeException.class,
            () -> gameService.join(kalahGame.getGameReference(), new PlayerDto(playerTwo)));

    Assertions.assertEquals("Game already has two players!", runtimeException.getMessage());
  }
}
