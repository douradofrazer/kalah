package com.fraz.demo.kalah.controller;

import com.fraz.demo.kalah.domain.KalahGame;
import com.fraz.demo.kalah.domain.constant.GameStatus;
import com.fraz.demo.kalah.service.GameService;
import com.fraz.demo.kalah.transferobj.BoardStateDto;
import com.fraz.demo.kalah.transferobj.GameDto;
import com.fraz.demo.kalah.transferobj.PitDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
public class GameControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private GameService gameService;

  @Test
  public void testInitGame() throws Exception {

    UUID gameReference = UUID.randomUUID();

    when(gameService.init(any())).thenReturn(new GameDto(gameReference, "outlaw_24", null));

    this.mockMvc
        .perform(
            post("/v1/games/init")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    """
                    {
                      "userName": "outlaw_24"
                    }
                    """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.gameReference").value(gameReference.toString()))
        .andExpect(jsonPath("$.playerOne").value("outlaw_24"))
        .andExpect(jsonPath("$.playerTwo").isEmpty());
  }

  @Test
  void testShouldReturnGameState() throws Exception {
    KalahGame kalahGame = new KalahGame("outlaw_24");
    kalahGame.joinGame(kalahGame.getGameReference().toString(), "megamind19");

    AtomicInteger count = new AtomicInteger(0);

    when(gameService.getBoardState(any()))
        .thenReturn(
            new BoardStateDto(
                kalahGame.getGameReference().toString(),
                kalahGame.getGameStatus().toString(),
                kalahGame.getCurrentPlayer(),
                Arrays.stream(kalahGame.getBoard().getPits())
                    .map(pit -> new PitDto(count.getAndIncrement(), pit.getOwner(), pit.getSeeds()))
                    .toList(),
                null));

    this.mockMvc
        .perform(
            get("/v1/games/{gameRef}/status", kalahGame.getGameReference())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.gameRef").value(kalahGame.getGameReference().toString()))
        .andExpect(jsonPath("$.status").value(GameStatus.IN_PROGRESS.toString()))
        .andExpect(jsonPath("$.currentPlayer").value("outlaw_24"))
        .andExpect(jsonPath("$.pits[0].houseIndex").value(0))
        .andExpect(jsonPath("$.pits[0].seeds").value(6))
        .andExpect(jsonPath("$.pits[0].player").value("outlaw_24"));
  }

  @Test
  public void testShouldAllowToJoinGame() throws Exception {
    UUID gameReference = UUID.randomUUID();

    when(gameService.join(any(), any()))
        .thenReturn(new GameDto(gameReference, "outlaw_24", "megamind19"));

    this.mockMvc
        .perform(
            put("/v1/games/{gameRef}/join", gameReference)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    """
                    {
                      "userName":"megamind19"
                    }
                    """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.gameReference").value(gameReference.toString()))
        .andExpect(jsonPath("$.playerOne").value("outlaw_24"))
        .andExpect(jsonPath("$.playerTwo").value("megamind19"));
  }

  @Test
  public void testMakeMove() throws Exception {

    KalahGame kalahGame = new KalahGame("outlaw_24");
    kalahGame.joinGame(kalahGame.getGameReference().toString(), "megamind19");
    kalahGame.makeMove("outlaw_24", 1);

    when(gameService.makeMove(any(), any()))
        .thenReturn(
            new BoardStateDto(
                kalahGame.getGameReference().toString(),
                kalahGame.getGameStatus().toString(),
                kalahGame.getCurrentPlayer(),
                Arrays.asList(
                    new PitDto(0, "outlaw_24", 6),
                    new PitDto(1, "outlaw_24", 0)), // added only one pit for testing
                "winner"));

    this.mockMvc
        .perform(
            put("/v1/games/{gameRef}/move", kalahGame.getGameReference())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    """
                    {
                      "userName":"outlaw_24",
                      "houseIndex":1
                    }
                    """))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.gameRef").value(kalahGame.getGameReference().toString()))
        .andExpect(jsonPath("$.status").value(GameStatus.IN_PROGRESS.toString()))
        .andExpect(jsonPath("$.currentPlayer").value("megamind19"))
        .andExpect(jsonPath("$.pits[1].houseIndex").value(1))
        .andExpect(jsonPath("$.pits[1].seeds").value(0));
  }
}
