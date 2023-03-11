package it.unipr.desantisinvitto;

import java.util.Random;

public class PriceGeneratorThread implements Runnable{
	
	private Server server;
	private int price;
	private boolean stop;
	private static final int MAX_PRICE = 200;
	private static final int MIN_PRICE = 10;
	
	public PriceGeneratorThread(final Server s) {
		this.server = s;
		this.stop = false;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Random random = new Random();
		while(!stop) {
			try {
				this.price = random.nextInt(MAX_PRICE-MIN_PRICE) + MIN_PRICE;
				Thread.sleep(5000);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public int getPrice() {
		return this.price;
	}
	
	public void setStop(boolean value) {
		this.stop = value;
	}

}
