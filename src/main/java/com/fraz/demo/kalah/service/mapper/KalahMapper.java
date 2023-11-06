package com.fraz.demo.kalah.service.mapper;

import com.fraz.demo.kalah.domain.Board;
import com.fraz.demo.kalah.domain.KalahGame;
import com.fraz.demo.kalah.repository.model.Kalah;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class KalahMapper {

  public abstract Kalah toKalah(KalahGame kalahGame);

  public abstract KalahGame toKalahGame(Kalah kalah);

  Board map(Kalah.Board board) {
    return new Board(board.getPits());
  }
}
