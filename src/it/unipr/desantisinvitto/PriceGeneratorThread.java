package it.unipr.desantisinvitto;

import java.util.Random;

/**
 * The {@code PriceGeneratorThread} class defines the behavior of the
 * thread created by the server that manages the price of the product.
 * 
 * In particular the thread periodically changes the price, which is
 * randomly generated.
 * 
 * @author De Santis Fabrizio, Invitto Francesco
 *
 */
public class PriceGeneratorThread implements Runnable{
	
	private int price;
	private boolean stop;
	private static final int MIN_PRICE = 10;
	private static final int MAX_PRICE = 200;
	
	/**
	 * Class constructor.
	 * 
	 */
	public PriceGeneratorThread() {
		this.stop = false;
	}

	@Override
	public void run() {
		Random random = new Random();
		
		while(!stop) {
			try {
				this.price = random.nextInt(MAX_PRICE - MIN_PRICE) + MIN_PRICE;	//random generation of the new price
				Thread.sleep(1500);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * This method is used by the server threads to retrieve the current
	 * price of the product.
	 * 
	 * @return the current price of the product
	 */
	public int getPrice() {
		return this.price;
	}
	
	/**
	 * This method is called by the last server thread in order to tell
	 * the generator to stop its execution.
	 * 
	 * @param value	true to stop the execution
	 */
	public void setStop(boolean value) {
		this.stop = value;
	}

}
