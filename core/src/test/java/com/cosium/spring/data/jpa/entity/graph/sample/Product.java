package com.cosium.spring.data.jpa.entity.graph.sample;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedEntityGraphs;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
@NamedEntityGraphs(
    value = {
      @NamedEntityGraph(
          name = Product.BRAND_EG,
          attributeNodes = {@NamedAttributeNode("brand")})
    })
@Entity
public class Product {

  public static final String BRAND_EG = "Product.brand";

  public static final String BRAND_PROP_NAME = "brand";
  public static final String MAKER_PROP_NAME = "maker";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Access(value = AccessType.PROPERTY)
  private long id = 0;

  private String name;

  private String barcode;

  @ManyToOne(fetch = FetchType.LAZY)
  private Brand brand;

  @ManyToOne(fetch = FetchType.LAZY)
  private Maker maker;

  @ManyToOne(fetch = FetchType.EAGER)
  private Category category;

  // The sole presence of this field ensures
  // https://github.com/Cosium/spring-data-jpa-entity-graph/issues/68 correction
  // Should not be removed
  private byte[] image;

  @ElementCollection
  @MapKeyColumn(name = "name")
  @Column(name = "value")
  @CollectionTable(name = "properties", joinColumns = @JoinColumn(name = "product_id"))
  private final Map<String, String> properties = new HashMap<>();

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

  public Brand getBrand() {
    return brand;
  }

  public void setBrand(Brand brand) {
    this.brand = brand;
  }

  public Maker getMaker() {
    return maker;
  }

  public void setMaker(Maker maker) {
    this.maker = maker;
  }

  public String getBarcode() {
    return barcode;
  }

  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public Map<String, String> getProperties() {
    return properties;
  }
}
