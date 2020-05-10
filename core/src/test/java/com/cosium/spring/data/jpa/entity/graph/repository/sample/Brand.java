package com.cosium.spring.data.jpa.entity.graph.repository.sample;

import javax.persistence.*;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
@NamedEntityGraphs(
  value = {
    @NamedEntityGraph(
      name = Brand.EMPTY_EG,
      attributeNodes = {}
    )
  }
)
@Entity
public class Brand {

  public static final String EMPTY_EG = "Brand.empty";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Access(value = AccessType.PROPERTY)
  private long id = 0;

  private String name;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
