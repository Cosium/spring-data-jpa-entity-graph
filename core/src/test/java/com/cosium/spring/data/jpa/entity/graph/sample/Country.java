package com.cosium.spring.data.jpa.entity.graph.sample;

import jakarta.persistence.*;

/**
 * Created on 17/03/17.
 *
 * @author Reda.Housni-Alaoui
 */
@Entity
public class Country {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id = 0;

  private String name;

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
