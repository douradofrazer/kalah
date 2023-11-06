package com.fraz.demo.kalah.repository.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.generator.EventType;
import org.hibernate.type.SqlTypes;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "game")
@Getter
@Setter
public class Game {
  // TODO: fix this, it isn't the right entity class
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "reference", columnDefinition = "uuid", nullable = false)
  private UUID reference;

  @Column(name = "snapshot")
  @JdbcTypeCode(SqlTypes.JSON)
  private Kalah snapshot;

  // https://docs.jboss.org/hibernate/orm/6.3/javadocs/org/hibernate/annotations/Generated.html
  @Generated(event = {EventType.INSERT})
  @Column(name = "created_at", updatable = false, insertable = false)
  private OffsetDateTime createdAt;
}
