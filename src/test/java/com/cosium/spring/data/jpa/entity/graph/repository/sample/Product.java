package com.cosium.spring.data.jpa.entity.graph.repository.sample;

import javax.persistence.*;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
@NamedEntityGraphs(value = {
		@NamedEntityGraph(name = Product.PRODUCT_BRAND_EG, attributeNodes = {
			@NamedAttributeNode("brand")
		})
})
@Entity
public class Product {

	public static final String PRODUCT_BRAND_EG = "Product.brand";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Access(value = AccessType.PROPERTY)
	private long id = 0;

	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	private Brand brand;

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
}
