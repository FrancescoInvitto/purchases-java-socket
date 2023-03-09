package it.unipr.desantisinvitto;

import java.io.Serializable;

public class Price implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private final int price;
	
	public Price(final int p) {
		this.price = p;
	}
	
	public int getPrice() {
		return this.price;
	}
}
