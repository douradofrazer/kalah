package com.fraz.demo.kalah.repository.model;

import com.fraz.demo.kalah.domain.Pit;
import com.fraz.demo.kalah.domain.constant.GameStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import java.util.UUID;

@RedisHash("Kalah")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Kalah {
  @Id private UUID gameReference;
  private Board board;
  private String playerOne;
  private String playerTwo;
  private String currentPlayer;
  private GameStatus gameStatus;
  private String winner;

  @Getter
  @Setter
  public static class Board {
    private Pit[] pits;
  }
}
