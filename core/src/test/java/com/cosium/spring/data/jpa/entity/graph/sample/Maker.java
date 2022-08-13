package com.cosium.spring.data.jpa.entity.graph.sample;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;

/**
 * Created on 24/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
@NamedEntityGraphs({
  @NamedEntityGraph(
      name = Maker.COUNTRY_EG,
      attributeNodes = {@NamedAttributeNode("country")})
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

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "maker")
  private final Set<Product> products = new LinkedHashSet<>();

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

  public Set<Product> getProducts() {
    return products;
  }
}
