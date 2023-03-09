package it.unipr.desantisinvitto;

import java.io.Serializable;

public class Offer implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private final int offer;
	
	public Offer(final int o) {
		this.offer = o;
	}
	
	public int getOffer() {
		return this.offer;
	}

}
