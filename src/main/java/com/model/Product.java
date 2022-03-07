package com.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Product {
	
	@Id@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	@ManyToOne
	private Brand brand;
	private BigDecimal pricePattern;
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
	public BigDecimal getPricePattern() {
		return pricePattern;
	}
	public void setPricePattern(BigDecimal pricePattern) {
		this.pricePattern = pricePattern;
	}

}
