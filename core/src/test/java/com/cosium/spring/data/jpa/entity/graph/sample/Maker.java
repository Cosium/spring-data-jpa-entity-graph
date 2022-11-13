package com.cosium.spring.data.jpa.entity.graph.sample;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedEntityGraphs;
import jakarta.persistence.OneToMany;
import java.util.LinkedHashSet;
import java.util.Set;

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
