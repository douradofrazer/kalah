package com.fraz.demo.kalah.repository;

import com.fraz.demo.kalah.repository.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigInteger;

@Repository
@Transactional
public interface GameRepository extends JpaRepository<Game, BigInteger> {}
