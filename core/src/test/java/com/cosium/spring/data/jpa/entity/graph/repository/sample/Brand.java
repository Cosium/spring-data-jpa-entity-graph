package com.cosium.spring.data.jpa.entity.graph.repository.sample;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;

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

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "brand")
  @MapKey
  private final Map<Long, Product> products = new LinkedHashMap<>();

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

  public Map<Long, Product> getProducts() {
    return products;
  }
}
