package com.fraz.demo.kalah.service;

import com.fraz.demo.kalah.transferobj.*;
import java.util.*;

public interface GameService {
  GameDto init(PlayerDto player);

  GameDto join(UUID gameRef, PlayerDto player);

  BoardStateDto getBoardState(String gameRef);

  BoardStateDto makeMove(UUID gameRef, GameMoveDto move);
}
