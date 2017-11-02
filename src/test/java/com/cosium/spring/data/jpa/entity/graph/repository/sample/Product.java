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
      name = Product.DEFAULT_EG,
      attributeNodes = {@NamedAttributeNode("maker")}
    ),
    @NamedEntityGraph(
      name = Product.BRAND_EG,
      attributeNodes = {@NamedAttributeNode("brand")}
    )
  }
)
@Entity
public class Product {

  public static final String DEFAULT_EG = "Product.default";
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
}
