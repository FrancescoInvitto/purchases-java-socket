package it.unipr.desantisinvitto;

import java.io.Serializable;

/**
 * The {@code Price} class defines the price of the product.
 * 
 * @author De Santis Fabrizio, Invitto Francesco
 *
 */
public class Price implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private final int value;
	
	/**
	 * Class constructor.
	 * 
	 * @param v	the value of the price
	 */
	public Price(final int v) {
		this.value = v;
	}
	
	/**
	 * This method is used by the clients to retrieve
	 * the current price of the product.
	 * 
	 * @return	the current product price
	 */
	public int getValue() {
		return this.value;
	}
}
