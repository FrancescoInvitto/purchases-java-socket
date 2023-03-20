package it.unipr.desantisinvitto;

import java.io.Serializable;

/**
 * The {@code Offer} class defines the offer made by the clients.
 * 
 * @author De Santis Fabrizio, Invitto Francesco
 *
 */
public class Offer implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private final int value;
	
	/**
	 * Class constructor.
	 * 
	 * @param v	the value of the offer
	 */
	public Offer(final int v) {
		this.value = v;
	}
	
	/**
	 * This method is used by the server thread to retrieve the value
	 * of the client's offer.
	 * 
	 * @return the value of the offer
	 */
	public int getValue() {
		return this.value;
	}

}
