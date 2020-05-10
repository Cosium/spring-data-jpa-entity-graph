package com.cosium.spring.data.jpa.entity.graph.repository.sample;

import javax.persistence.*;

/**
 * Created on 24/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
@NamedEntityGraphs({
  @NamedEntityGraph(
    name = Maker.COUNTRY_EG,
    attributeNodes = {@NamedAttributeNode("country")}
  )
})
@Entity
public class Maker {

  public static final String COUNTRY_EG = "Marker.country";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Access(value = AccessType.PROPERTY)
  private long id = 0;

  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private Country country;

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

  public Country getCountry() {
    return country;
  }

  public void setCountry(Country country) {
    this.country = country;
  }
}
