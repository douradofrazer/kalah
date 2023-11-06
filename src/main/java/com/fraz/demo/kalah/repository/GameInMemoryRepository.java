package com.fraz.demo.kalah.repository;

import com.fraz.demo.kalah.repository.model.Kalah;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface GameInMemoryRepository extends KeyValueRepository<Kalah, UUID> {}
